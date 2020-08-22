package com.raddyr.products.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.model.ImageUploadResponse
import com.raddyr.products.data.model.Product
import com.raddyr.products.ui.base.BaseProductActivity
import com.raddyr.products.ui.details.ProductDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.base_product_activity.*
import javax.inject.Inject


@AndroidEntryPoint
class EditProductActivity(override val contentViewLayout: Int = R.layout.base_product_activity) :
    BaseProductActivity<EditProductViewModel>() {

    @Inject
    lateinit var tokenUtil: TokenUtil


    override fun afterView() {
        productToEdit = (intent.getParcelableExtra(PRODUCT_DETAILS))
        super.afterView()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        configureView()
        setButton()
        setCheckbox()
        setupObservers()
    }

    override fun setViewModel() {
        viewModel = viewModelFactory.create(EditProductViewModel::class.java)
    }

    private fun setButton() {
        if (!intent.getBooleanExtra(IS_TO_EDIT, false)) {
            button.text = getString(R.string.save)
            button.setOnClickListener {
                if (imageFile != null) {
                    invokeImageUploadRequest()
                } else {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().apply { putExtra(PRODUCT_DETAILS, getDataFromViews()) })
                    finish()
                }
            }
        }
    }

    private fun setCheckbox() {
        checkbox.isChecked = productToEdit?.metadata?.shared == true
    }

    override fun invokeRequest(product: Product) {
            productToEdit?.let { productToEdit ->
                product.apply {
                    this.uuid = productToEdit.uuid
                    this.quantity = productToEdit.quantity
                    this.metadata.let { metadata ->
                        metadata?.isSynchronized = productToEdit.metadata?.isSynchronized
                        metadata?.toBeDeleted = productToEdit.metadata?.toBeDeleted
                        metadata?.createdDate = productToEdit.metadata?.createdDate
                    }
                }
            }
        viewModel.editRequest.value = product
    }

    private fun setupObservers() {

        responseHandler.observe(this, viewModel.editResponse, object : Callback<Product> {
            override fun onLoaded(data: Product) {
                startActivity(
                    ProductDetailsActivity.prepareIntentWithUIID(
                        this@EditProductActivity,
                        data.uuid.toString()
                    )
                )
                finish()
            }
        })
    }

    override fun customCallback(uuid: String) {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(
                    PRODUCT_DETAILS,
                    getDataFromViews().apply {
                        productCard?.setPhotoUrl(
                            getImageUri(
                            ) + uuid
                        )
                    }
                )
            })
        finish()
    }

    private fun configureView() {
        if (intent.getBooleanExtra(IS_TO_EDIT, false)) {
            button.text = getString(R.string.edit)
        }
        intent.getParcelableExtra<Product>(PRODUCT_DETAILS)?.let {
            it.productCard?.photoUrl?.let { photo ->
                val glideUrl =
                    GlideUrl(photo) { mapOf(Pair("Authorization", tokenUtil.getToken())) }
                Glide
                    .with(this)
                    .load(glideUrl)
                    .error(R.drawable.placeholder)
                    .into(image)
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {

        fun prepareIntent(
            context: Context,
            product: Product,
            isToEdit: Boolean = false
        ): Intent {
            return Intent(context, EditProductActivity::class.java).apply {
                putExtra(PRODUCT_DETAILS, product)
                putExtra(IS_TO_EDIT, isToEdit)
            }
        }
    }
}