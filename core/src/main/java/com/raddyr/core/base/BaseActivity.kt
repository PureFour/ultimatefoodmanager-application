package com.raddyr.core.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.raddyr.core.R
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.data.rx.Subscribers
import com.raddyr.core.ui.dialogManager.DialogManager
import com.raddyr.core.util.internet.InternetConnectionService
import com.raddyr.core.util.internet.InternetConnectionService.Companion.INTERNET_STATUS
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.sharedPreferences.SharedPreferencesUtil
import com.raddyr.core.util.sync.AccountGeneral
import com.raddyr.core.util.sync.SyncAdapter
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var responseHandler: ResponseHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    @Inject
    lateinit var activitiesAndFragmentConfig: ActivitiesAndFragmentConfig

    private lateinit var intentFilter: IntentFilter

    private var internetWasOff = false

    val subscriptionManager: Subscribers.Companion.SubscriptionManager by lazy { Subscribers.Companion.SubscriptionManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewLayout)
        dialogManager.bindActivity(this)
        window.navigationBarColor = this.getColor(R.color.background_color)
        window.decorView.systemUiVisibility = 0
        afterView()
        startCheckingInternetService()
        AccountGeneral.createSyncAccount(this)
        SyncAdapter.performSync()
    }

    private fun startCheckingInternetService() {
        intentFilter = IntentFilter().apply {
            addAction(BroadCastStringForAction)
            val serviceIntent = Intent(this@BaseActivity, InternetConnectionService::class.java)
            startService(serviceIntent)
        }
    }

    protected abstract val contentViewLayout: Int

    protected abstract fun afterView()

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideProgressBar()
        subscriptionManager.unregisterAll()
    }

    private val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BroadCastStringForAction) {
                val internetActualStatus = intent.getBooleanExtra(INTERNET_STATUS, false)
                if (internetActualStatus) {
                    if (internetWasOff) {
                        SyncAdapter.performSync()
                        internetWasOff = false
                    }
                } else {
                    internetWasOff = true
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        registerReceiver(myReceiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(myReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myReceiver)
    }

    companion object {
        const val BroadCastStringForAction = "CHECK_INTERNET"
    }
}