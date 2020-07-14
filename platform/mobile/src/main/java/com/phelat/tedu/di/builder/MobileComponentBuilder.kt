package com.phelat.tedu.di.builder

import com.phelat.tedu.dependencyinjection.ComponentBuilder
import com.phelat.tedu.dependencyinjection.StartupTasks
import com.phelat.tedu.di.component.DaggerMobileComponent
import com.phelat.tedu.di.component.MobileComponent

object MobileComponentBuilder : ComponentBuilder<MobileComponent>() {

    override fun initializeComponent(addStartupTask: (StartupTasks) -> Unit): MobileComponent {
        return DaggerMobileComponent.create()
    }
}