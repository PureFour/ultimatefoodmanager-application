package com.raddyr.authentication.ui.splash

import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.viewModels
import com.raddyr.authentication.R
import com.raddyr.authentication.data.model.ServerStatus
import com.raddyr.authentication.ui.AuthActivity
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.data.network.interceptors.NetworkConnectionInterceptor
import com.raddyr.core.util.extensions.displayInfoDialog
import com.raddyr.core.util.extensions.displayQuestionDialog
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.sync.SyncAdapter
import com.raddyr.core.util.tokenUtil.TokenUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity(override val contentViewLayout: Int = R.layout.splash_screen_activity) :
    BaseActivity() {

    @Inject
    lateinit var tokenUtil: TokenUtil

    @Inject
    lateinit var interceptor: NetworkConnectionInterceptor

    private val viewModel by viewModels<SplashScreenViewModel> { viewModelFactory }

    override fun afterView() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        responseHandler.observe(this, viewModel.response, object : Callback<ServerStatus> {
            override fun onLoaded(data: ServerStatus) {
                SyncAdapter.performSync()
                handleResponse(data)
            }

            override fun onNoInternet(): Boolean {
                noInternetHandling()
                return false
            }

            override fun onError(throwable: Throwable): Boolean {
                onNoInternet()
                return false
            }
        }, false)
    }

    private fun handleResponse(response: ServerStatus) {
        if (response.status == SERVER_STATUS) {
            val bootSplashAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.boot_splash)
            val imageView: ImageView = findViewById(R.id.bootSplashImage)
            imageView.animation = bootSplashAnimation
            Handler().postDelayed({
                startActivity(
                    if (!tokenUtil.isTokenAvailable())
                        Intent(
                            this@SplashScreenActivity,
                            AuthActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) else activitiesAndFragmentConfig.homeActivity(
                        this
                    ).get()
                )
                finish()
            }, 1000)
        }
    }

    fun noInternetHandling() {
        if (tokenUtil.isTokenAvailable()) {
            displayQuestionDialog(
                getString(R.string.no_internet),
                positiveButtonTitle = getString(R.string.continue_offline),
                negativeButtonTitle = getString(R.string.close_app),
                cancelable = false,
                negativeListener = DialogInterface.OnClickListener { _, _ ->
                    finish()
                },
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    startActivity(
                        activitiesAndFragmentConfig.homeActivity(this@SplashScreenActivity).get()
                    )
                    finish()
                })
        } else {
            val dialog: (listener: DialogInterface.OnClickListener) -> Unit = {
                displayInfoDialog(
                    title = getString(R.string.turn_on_internet),
                    message = getString(R.string.turn_on_internet_description),
                    buttonString = getString(R.string.try_again),
                    cancelable = false,
                    listener = it
                )
            }
            var isDisplayed = false
            val displayDialog: () -> Unit = {
                while (!interceptor.isInternetEnabled()) {
                    if (!isDisplayed) {
                        isDisplayed = true
                        runOnUiThread {
                            dialog.invoke(DialogInterface.OnClickListener { _, _ ->
                                isDisplayed = false
                                if (interceptor.isInternetEnabled()) {
                                    startActivity(
                                        Intent(
                                            this@SplashScreenActivity,
                                            AuthActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            })
                        }
                    }
                }
            }
            subscriptionManager.register(
                Single.fromCallable(displayDialog).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
            )
        }
    }

    companion object {
        private const val SERVER_STATUS = "UP"
    }
}