package com.raddyr.ufm.di

import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.config.AppBuildConfig
import com.raddyr.core.config.SettingsConfig
import com.raddyr.core.push.FirebaseMessagingManager
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.logout.LogoutManager
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.push.service.CustomFirebaseMessagingService
import com.raddyr.ufm.config.UfmActivitiesAndFragmentConfig
import com.raddyr.ufm.config.UfmAppBuildConfig
import com.raddyr.ufm.config.UfmSettingsConfig
import com.raddyr.ufm.logout.LogoutManagerImpl
import com.raddyr.ufm.util.formatter.AppDateFormatterUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UfmModule {

    @Provides
    @Singleton
    fun bindAppConfig(): AppBuildConfig {
        return UfmAppBuildConfig()
    }

    @Provides
    fun getActivitiesAndFragmentConfig(): ActivitiesAndFragmentConfig {
        return UfmActivitiesAndFragmentConfig()
    }

    @Provides
    fun getSettingsList(): SettingsConfig {
        return UfmSettingsConfig()
    }

    @Provides
    fun firebaseService(): FirebaseMessagingManager {
        return CustomFirebaseMessagingService()
    }


    @Provides
    fun logoutManager(
        tokenUtil: TokenUtil,
        productDao: ProductDao,
        activitiesAndFragment: ActivitiesAndFragmentConfig,
        dataCache: DataCache
    ): LogoutManager {
        return LogoutManagerImpl(tokenUtil, productDao, activitiesAndFragment, dataCache)
    }

    @Provides
    fun dateFormatter(): DateFormatterUtils {
        return AppDateFormatterUtils()
    }
}