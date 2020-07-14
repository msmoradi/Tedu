package com.phelat.tedu.di.component

import com.phelat.tedu.androiddagger.DispatcherComponent
import com.phelat.tedu.dependencyinjection.platform.PlatformScope
import com.phelat.tedu.di.module.GraphModule
import com.phelat.tedu.di.module.MobileInjectorModule
import dagger.Component
import dagger.android.AndroidInjectionModule

@PlatformScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        MobileInjectorModule::class,
        GraphModule::class
    ]
)
interface MobileComponent : DispatcherComponent