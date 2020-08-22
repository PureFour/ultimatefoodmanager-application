package com.raddyr.products.di

import android.content.Context
import androidx.room.Room
import com.raddyr.core.ui.dialogManager.DialogManager
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.core.util.sharedPreferences.SharedPreferencesUtil
import com.raddyr.products.data.db.ProductDatabase
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.products.data.repository.ProductRepository
import com.raddyr.products.network.ProductServiceApiProvider
import com.raddyr.products.ui.utils.DataCacheUtil
import com.raddyr.products.ui.utils.SharedInfoServiceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ProductDatabase {
        return Room.databaseBuilder(
            appContext,
            ProductDatabase::class.java,
            "product_database"
        ).build()
    }

    @Provides
    fun providesProductDao(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.productDao()
    }

    @Provides
    fun getSharedProductsUtil(sharedPreferencesUtil: SharedPreferencesUtil): DataCache {
        return DataCacheUtil(sharedPreferencesUtil)
    }

    @Provides
    fun getSharedProductInfo(
        repository: ProductRepository,
        dataCache: DataCache,
        provider:ProductServiceApiProvider
    ): SharedInfoService {
        return SharedInfoServiceUtil(repository, dataCache, provider)
    }
}