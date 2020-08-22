package com.raddyr.ufm.logout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.data.rx.Subscribers
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.logout.LogoutManager
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.data.db.dao.ProductDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LogoutManagerImpl @Inject constructor(
    private val tokenUtil: TokenUtil,
    private val productDao: ProductDao,
    private val activityAndFragment: ActivitiesAndFragmentConfig,
    private val dataCache: DataCache
) : LogoutManager {
    override fun logout(
        context: Context,
        subscriptionManager: Subscribers.Companion.SubscriptionManager
    ) {
        subscriptionManager.register(
            productDao.deleteAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe()
        )
        dataCache.clearInfo(context as AppCompatActivity)
        tokenUtil.deleteToken()
        context.startActivity(
            activityAndFragment.authActivity(context).get()
        )
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}