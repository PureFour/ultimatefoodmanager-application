package com.raddyr.products.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.raddyr.core.data.rx.Subscribers
import com.raddyr.core.util.sync.SyncAdapter
import com.raddyr.products.R
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.network.ProductServiceApiProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SyncService : Service() {

    @Inject
    lateinit var provider: ProductServiceApiProvider

    @Inject
    lateinit var productDao: ProductDao

    private val subscriptionManager: Subscribers.Companion.SubscriptionManager by lazy { Subscribers.Companion.SubscriptionManager() }

    val callback: () -> Unit = {
        subscriptionManager.observe(productDao.getNotSync(), {
            if (!it.isNullOrEmpty()) {
                subscriptionManager.observe(
                    provider.get()!!
                        .sync(it.map { productEntity -> ProductMapper.getProduct(productEntity) }),
                    { data ->
                        subscriptionManager.observe(
                            productDao.deleteAll().flatMap {
                                productDao.insert(data.synchronizedProducts.map { product ->
                                    ProductMapper.getProductEntity(product)
                                }.toList())
                            }, {
                                val message = if (data.status == CONFLICT_STATUS) getString(R.string.synchronized_with_conflict)
                                     else getString(R.string.sync_success)
                                Timber.i("Database Synchronized")
                                Toast.makeText(
                                    applicationContext,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }, { e -> Timber.e(e) })
                    },
                    { exception ->
                        Timber.e(exception)
                    })
            }
        },
            { e ->
                Timber.e(e)
            })
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(sSyncAdapterLock) {
            sSyncAdapter = sSyncAdapter
                ?: SyncAdapter(
                    applicationContext,
                    callback,
                    true
                )
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return sSyncAdapter?.syncAdapterBinder ?: throw IllegalStateException()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        subscriptionManager.unregisterAll()
        return super.onUnbind(intent)
    }

    companion object {
        private var sSyncAdapter: SyncAdapter? = null
        private val sSyncAdapterLock = Any()
        private val CONFLICT_STATUS = "CONFLICT"
    }
}