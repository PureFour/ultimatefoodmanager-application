package com.raddyr.products

import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.config.AppBuildConfig
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.products.data.db.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestModule {

    @Provides
    @Singleton
    fun bindAppConfig(): AppBuildConfig {
        return FakeAppBuildConfig()
    }

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ProductDatabase::class.java).allowMainThreadQueries()
            .build()

    @Provides
    fun dateFormatter(): DateFormatterUtils {
        return AppDateFormatterUtils()
    }

    @Provides
    fun getActivitiesAndFragmentConfig(): ActivitiesAndFragmentConfig {
        return FakeActivitiesAndFragmentConfig()
    }

    class AppDateFormatterUtils() : DateFormatterUtils("")


    class FakeAppBuildConfig : AppBuildConfig() {
        override fun SERVER_URL() = ""
        override fun API_URL() = ""
        override fun PORT() = ""
        override fun IS_DEBUG() = true
    }

    class FakeActivitiesAndFragmentConfig @Inject constructor() : ActivitiesAndFragmentConfig() {

        override fun authActivity(context: Context) = Optional.empty<Intent>()

        override fun homeActivity(context: Context) = Optional.empty<Intent>()

        override fun productDetailsActivity(context: Context, contents: String) =
            Optional.empty<Intent>()

        override fun shareSettingsActivity(context: Context, data: String) =
            Optional.empty<Intent>()
    }
}