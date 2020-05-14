package com.phelat.tedu.settings.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.phelat.tedu.androiddagger.inject
import com.phelat.tedu.designsystem.component.view.BottomSheetView
import com.phelat.tedu.designsystem.entity.BottomSheetItemEntity
import com.phelat.tedu.settings.R
import com.phelat.tedu.settings.di.component.SettingsComponent
import com.phelat.tedu.settings.viewmodel.SettingsViewModel
import com.phelat.tedu.uiview.observeNavigation
import kotlinx.android.synthetic.main.fragment_settings.backupClick
import kotlinx.android.synthetic.main.fragment_settings.userInterfaceMode
import kotlinx.android.synthetic.main.fragment_settings.userInterfaceModeClick
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var settingsViewModelFactory: ViewModelProvider.Factory

    private val settingsViewModel: SettingsViewModel by viewModels { settingsViewModelFactory }

    private var userInterfaceModeSheet: BottomSheetView? = null
    private var backupMethodSheet: BottomSheetView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject<SettingsComponent>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userInterfaceModeClick.setOnClickListener {
            settingsViewModel.onChangeUserInterfaceModeClick()
        }
        backupClick.setOnClickListener {
            settingsViewModel.onBackUpClick()
        }
        settingsViewModel.apply {
            navigationObservable.observeNavigation(this@SettingsFragment)
            userInterfaceSheetObservable.observe(
                viewLifecycleOwner,
                ::observeUserInterfaceModeSheet
            )
            backupMethodSheetObservable.observe(
                viewLifecycleOwner,
                ::observeBackupMethodSheet
            )
            userInterfaceTitleObservable.observe(viewLifecycleOwner, userInterfaceMode::setText)
        }
    }

    private fun observeUserInterfaceModeSheet(items: List<BottomSheetItemEntity>) {
        userInterfaceModeSheet?.apply {
            sheetItems = items
            show()
        } ?: run {
            userInterfaceModeSheet = BottomSheetView(requireContext()).apply {
                sheetTitle = R.string.settings_ui_mode_sheet_title
            }
            observeUserInterfaceModeSheet(items)
        }
    }

    private fun observeBackupMethodSheet(items: List<BottomSheetItemEntity>) {
        backupMethodSheet?.apply {
            sheetItems = items
            show()
        } ?: run {
            backupMethodSheet = BottomSheetView(requireContext()).apply {
                sheetTitle = R.string.backup_method_title
            }
            observeBackupMethodSheet(items)
        }
    }

    override fun onDestroyView() {
        if (userInterfaceModeSheet?.isShowing == true) {
            userInterfaceModeSheet?.dismiss()
        }
        if (backupMethodSheet?.isShowing == true) {
            backupMethodSheet?.dismiss()
        }
        super.onDestroyView()
    }
}