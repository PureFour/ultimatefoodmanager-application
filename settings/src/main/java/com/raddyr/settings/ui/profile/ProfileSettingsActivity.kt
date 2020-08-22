package com.raddyr.settings.ui.profile

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.core.util.logout.LogoutManager
import com.raddyr.core.util.sharedPreferences.SharedPreferencesUtil
import com.raddyr.settings.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_settings_activity.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSettingsActivity(override val contentViewLayout: Int = R.layout.profile_settings_activity) :
    BaseActivity() {

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @Inject
    lateinit var logoutManager: LogoutManager

    @Inject
    lateinit var sharedInfoService: SharedInfoService

    override fun afterView() {
        setObserver()
        setProductsSharedInfo()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setupListeners()
    }

    private fun setupListeners() {
        logoutButton.setOnClickListener {
            logoutManager.logout(this, subscriptionManager)
            finish()
        }
    }

    private fun setObserver() {
        if (sharedPreferencesUtil.getStringPreferences(this, LOGIN)!!.isEmpty()) {
            sharedInfoService.fetchData(this) {
                setUserLabels()
            }
        } else {
            setUserLabels()
        }
    }

    private fun setUserLabels() {
        emailValue.text = sharedPreferencesUtil.getStringPreferences(this, EMAIL)
        loginValue.text = sharedPreferencesUtil.getStringPreferences(this, LOGIN)
    }

    private fun setProductsSharedInfo() {
        if (isProductsShared(this)) {
            productSharedValue.text = getString(R.string.text_yes)
            totalSharedProductsValue.text =
                sharedPreferencesUtil.getIntPreferences(this, TOTAL_SHARED).toString()
        } else {
            productSharedValue.text = getString(R.string.text_no)
            peopleProductsGroup.visibility = View.GONE
        }
        totalOwnProductsValue.text =
            sharedPreferencesUtil.getIntPreferences(this, TOTAL_OWN).toString()
        peopleValue.text = sharedPreferences.getStringListPreferences(
            this,
            USER_INFO
        ).takeIf { it?.isNotEmpty()!! }?.reduce { acc, s -> "$acc, $s" }
    }

    private fun isProductsShared(activity: AppCompatActivity) =
        sharedPreferencesUtil.getStringListPreferences(activity, USER_INFO)
            .let { !it.isNullOrEmpty() }

    companion object {
        private const val USER_INFO = "USER_INFO"
        private const val TOTAL_OWN = "TOTAL_OWN"
        private const val TOTAL_SHARED = "TOTAL_SHARED"
        private const val LOGIN = "LOGIN"
        private const val EMAIL = "EMAIL"
    }
}