package com.phelat.tedu.di.module

import com.phelat.tedu.MainActivity
import com.phelat.tedu.di.scope.MobileScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MobileInjectorModule {

    @MobileScope
    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity
}