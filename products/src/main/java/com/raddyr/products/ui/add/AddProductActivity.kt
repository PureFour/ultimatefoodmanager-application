package com.raddyr.products.ui.add

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.products.R
import com.raddyr.products.data.model.Product
import com.raddyr.products.ui.base.BaseProductActivity
import com.raddyr.products.ui.customViews.QuantitySelector
import com.raddyr.products.ui.details.ProductDetailsActivity.Companion.ACTION_AFTER_SEND
import com.raddyr.products.ui.status.StatusAfterSendActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.base_product_activity.*

@AndroidEntryPoint
class AddProductActivity(override val contentViewLayout: Int = R.layout.base_product_activity) :
    BaseProductActivity<AddProductViewModel>() {

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(ACTION_AFTER_SEND, result)
    }

    override fun afterView() {
        super.afterView()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setBarcode()
        setupObservers()
    }

    override fun setViewModel() {
        viewModel = viewModelFactory.create(AddProductViewModel::class.java)
    }

    private fun setBarcode() {
        barcode.setValue(intent.getStringExtra(BARCODE)).disable()
    }

    override fun invokeRequest(product: Product) {
        QuantitySelector(this) {
            viewModel.addRequest.value = List(it) { product }
        }
    }

    private fun setupObservers() {
        responseHandler.observe(
            this,
            viewModel.addResponse,
            object : Callback<Product> {
                override fun onLoaded(data: Product) {
                    startForResult.launch(
                        StatusAfterSendActivity.prepareIntent(
                            this@AddProductActivity,
                            data.productCard?.barcode
                        )
                    )
                }
            })
    }

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == RESULT_OK && requestCode == ACTION_AFTER_SEND) {
            setResult(Activity.RESULT_OK, result.data)
            finish()
        }
    }

    companion object {
        private const val BARCODE = "BARCODE"
        fun prepareIntent(context: Context, barcode: String?): Intent {
            return Intent(context, AddProductActivity::class.java).apply {
                putExtra(BARCODE, barcode)
            }
        }
    }
}