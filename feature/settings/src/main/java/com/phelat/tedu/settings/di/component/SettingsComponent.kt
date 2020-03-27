package com.phelat.tedu.settings.di.component

import com.phelat.tedu.androidcore.di.component.AndroidCoreComponent
import com.phelat.tedu.androiddagger.DispatcherComponent
import com.phelat.tedu.androiddagger.StartUpModule
import com.phelat.tedu.dependencyinjection.scope.FeatureScope
import com.phelat.tedu.settings.di.module.SettingsBindingModule
import com.phelat.tedu.settings.di.module.SettingsModule
import com.phelat.tedu.settings.di.module.SettingsStartUpModule
import com.phelat.tedu.settings.di.module.UserInterfaceModeModule
import dagger.Component
import dagger.android.AndroidInjectionModule

@FeatureScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        StartUpModule::class,
        SettingsStartUpModule::class,
        SettingsBindingModule::class,
        UserInterfaceModeModule::class,
        SettingsModule::class
    ],
    dependencies = [AndroidCoreComponent::class]
)
interface SettingsComponent : DispatcherComponent