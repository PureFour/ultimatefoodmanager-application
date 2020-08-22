package com.raddyr.scan.di

import androidx.lifecycle.ViewModel
import com.raddyr.core.di.viewModel.ViewModelKey
import com.raddyr.scan.ui.share.ShareSettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShareSettingsViewModel::class)
    abstract fun shareSettingsViewModel(viewModel: ShareSettingsViewModel): ViewModel
}