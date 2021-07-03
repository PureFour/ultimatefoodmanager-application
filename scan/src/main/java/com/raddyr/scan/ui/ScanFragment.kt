package com.raddyr.scan.ui

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.interfaces.Home
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.scan.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ScanFragment(override val contentViewLayout: Int = R.layout.scan_fragment) : BaseFragment() {

    @Inject
    lateinit var responseHandler: ResponseHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenUtil: TokenUtil

    @Inject
    lateinit var productDao: ProductDao

    @Inject
    lateinit var activityAndFragment: ActivitiesAndFragmentConfig

    @Inject
    lateinit var dataCache: DataCache

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}


    override fun afterView() {
        CaptureActivityPortrait.initiateScanForFragment(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        setProductsTab()
        result?.contents?.let {
            redirectToProperActivity(result.contents)
        }
    }

    private fun setProductsTab() {
        (activity as Home).getProducts()
    }

    private fun redirectToProperActivity(data: String) {
        try {
            UUID.fromString(data)
            startActivity(
                activityAndFragment.shareSettingsActivity(requireContext(), data).get()
            )
        } catch (e: IllegalArgumentException) {
            startForResult.launch(
                activityAndFragment.productDetailsActivity(
                    requireContext(),
                    data
                ).get()
            )
        }
    }

    companion object {
        private const val PRODUCT_DETAILS_CODE = 564
    }
}