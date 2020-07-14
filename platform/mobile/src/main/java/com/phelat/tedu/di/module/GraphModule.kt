package com.phelat.tedu.di.module

import com.phelat.tedu.R
import com.phelat.tedu.dependencyinjection.platform.PlatformScope
import com.phelat.tedu.entity.Graph
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module
class GraphModule {

    @Provides
    @PlatformScope
    @IntoMap
    @IntKey(R.navigation.navigation_todolist)
    fun provideTodoListGraph() = Graph(
        graphId = R.navigation.navigation_todolist,
        startDestination = R.id.todolist_fragment
    )

    @Provides
    @PlatformScope
    @IntoMap
    @IntKey(R.navigation.navigation_addtodo)
    fun provideAddTodoGraph() = Graph(
        graphId = R.navigation.navigation_addtodo,
        startDestination = R.id.addtodo_fragment
    )

    @Provides
    @PlatformScope
    @IntoMap
    @IntKey(R.navigation.navigation_settings)
    fun provideSettingsGraph() = Graph(
        graphId = R.navigation.navigation_settings,
        startDestination = R.id.settings_fragment
    )

    @Provides
    @PlatformScope
    @IntoMap
    @IntKey(R.navigation.navigation_backup)
    fun provideBackupGraph() = Graph(
        graphId = R.navigation.navigation_backup,
        startDestination = R.id.webdav_setup_fragment
    )
}