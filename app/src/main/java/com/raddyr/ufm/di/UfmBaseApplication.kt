package com.raddyr.ufm.di

import com.raddyr.core.base.BaseApplication
import com.raddyr.ufm.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class UfmBaseApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}