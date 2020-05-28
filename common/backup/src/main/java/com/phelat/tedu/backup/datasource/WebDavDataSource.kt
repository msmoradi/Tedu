package com.phelat.tedu.backup.datasource

import com.phelat.tedu.backup.entity.BackupTodoEntity
import com.phelat.tedu.backup.entity.WebDavCredentials
import com.phelat.tedu.backup.error.WebDavErrorContext
import com.phelat.tedu.datasource.Readable
import com.phelat.tedu.dependencyinjection.common.CommonScope
import com.phelat.tedu.functional.Failure
import com.phelat.tedu.functional.Response
import com.phelat.tedu.functional.Success
import com.thegrizzlylabs.sardineandroid.Sardine
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.Scanner
import javax.inject.Inject

@CommonScope
internal class WebDavDataSource @Inject constructor(
    private val sardine: Sardine
) : Readable.IO<WebDavCredentials, @JvmSuppressWildcards Response<BackupTodoEntity, WebDavErrorContext>> {

    override fun read(input: WebDavCredentials): Response<BackupTodoEntity, WebDavErrorContext> {
        sardine.setCredentials(input.username, input.password)
        return try {
            val url = getNormalizedUrl(input.url) + TEDU_BACKUP_FILE
            if (sardine.exists(url)) {
                sardine.get(url)
                    .use(::inputStreamToJsonObject)
                    .let(::backupContentToBackupEntity)
                    .let(::Success)
            } else {
                Failure(WebDavErrorContext.FileNotFound)
            }
        } catch (ignore: IOException) {
            Failure(WebDavErrorContext.GetFileFailed)
        } catch (ignore: JSONException) {
            Failure(WebDavErrorContext.CorruptedFile)
        }
    }

    private fun getNormalizedUrl(url: String): String {
        return if (url.last() == '/') {
            url
        } else {
            "$url/"
        }
    }

    private fun inputStreamToJsonObject(inputStream: InputStream): JSONObject {
        val scanner = Scanner(inputStream).useDelimiter("\\A")
        val builder = StringBuilder()
        while (scanner.hasNext()) {
            builder.append(scanner.next())
        }
        return JSONObject(builder.toString())
    }

    private fun backupContentToBackupEntity(content: JSONObject): BackupTodoEntity {
        return BackupTodoEntity(
            backupVersion = content.getInt(TEDU_BACKUP_VERSION),
            todos = content.getJSONObject(TEDU_BACKUP_TODOS)
        )
    }

    companion object {
        private const val TEDU_BACKUP_FILE = "tedu.backup"
        private const val TEDU_BACKUP_VERSION = "version"
        private const val TEDU_BACKUP_TODOS = "todos"
    }
}