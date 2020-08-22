package com.raddyr.products.ui.status

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.products.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.status_after_send_activity.*
import javax.inject.Inject

@AndroidEntryPoint
class StatusAfterSendActivity(override val contentViewLayout: Int = R.layout.status_after_send_activity) :
    BaseActivity() {

    override fun afterView() {
        setupListeners()
        info.text = String.format(
            getString(R.string.product_success_added), intent.getStringExtra(
                BARCODE
            )
        )
    }

    private fun setupListeners() {
        goToProductListButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(ACTION, PRODUCTS))
            finish()
        }
        searchButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(ACTION, SCAN))
            finish()
        }
    }

    companion object {
        fun prepareIntent(context: Context, barcode: String?): Intent {
            return Intent(context, StatusAfterSendActivity::class.java).apply {
                putExtra(BARCODE, barcode)
            }
        }
        const val ACTION = "ACTION"
        const val SCAN = "SCAN"
        private const val PRODUCTS = "PRODUCTS"
        private const val BARCODE = "BARCODE"
    }
}