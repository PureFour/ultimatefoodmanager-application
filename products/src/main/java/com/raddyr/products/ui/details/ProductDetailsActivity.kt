package com.raddyr.products.ui.details

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.model.Category
import com.raddyr.products.data.model.MeasurementUnit
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductMetadata
import com.raddyr.products.ui.add.AddProductActivity
import com.raddyr.products.ui.base.BaseProductActivity.Companion.PRODUCT_DETAILS
import com.raddyr.products.ui.customViews.ProductDetailsSection
import com.raddyr.products.ui.edit.EditProductActivity
import com.raddyr.products.ui.status.StatusAfterSendActivity
import com.raddyr.products.ui.status.StatusAfterSendActivity.Companion.ACTION
import com.raddyr.products.ui.status.StatusAfterSendActivity.Companion.SCAN
import com.raddyr.products.ui.utils.DataCacheUtil
import com.raddyr.products.ui.utils.DatePickerCustomTitle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.product_list_item.view.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ProductDetailsActivity(override val contentViewLayout: Int = R.layout.product_details_activity) :
    BaseActivity() {

    @Inject
    lateinit var tokenUtil: TokenUtil

    @Inject
    lateinit var dataCacheUtil: DataCacheUtil

    @Inject
    lateinit var dateFormatterUtils: DateFormatterUtils

    private val viewModel by lazy { viewModelFactory.create(ProductDetailsViewModel::class.java) }

    override fun afterView() {
        setupObservers()
        prepareView()
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun prepareView() {
        if (intent.hasExtra(UUID)) {
            viewModel.getRequest.value = intent.getStringExtra(UUID)
            checkboxGroup.visibility = GONE
        } else {
            toolbar.menu.findItem(R.id.edit_product).isVisible = false
            viewModel.searchRequest.value = intent.getStringExtra(BARCODE)
            if (!dataCacheUtil.isProductsShared(this)) {
                checkboxGroup.visibility = GONE
            } else {
                checkboxGroup.visibility = VISIBLE
            }
            setDataPicker()
        }
    }

    private val startForResultStatusAfterSend =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(ACTION_AFTER_SEND, result)
        }

    private val startForResultAddProduct =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(ACTION_AFTER_SEND, result)
        }

    private val startForResultEditProduct =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(EDIT_PRODUCT, result)
        }

    private fun setupObservers() {
        responseHandler.observe(
            this,
            viewModel.searchResponse,
            object : Callback<Product> {
                override fun onLoaded(data: Product) {
                    setupView(data)
                    buttonsGroup.visibility = VISIBLE
                    buttonFirst.text = getString(R.string.edit)
                    buttonSecond.text = getString(R.string.add)
                    buttonFirst.setOnClickListener {
                        startForResultEditProduct.launch(
                            EditProductActivity.prepareIntent(
                                this@ProductDetailsActivity,
                                viewModel.searchResponse.value?.data!!
                            )
                        )
                    }
                }

                override fun onError(throwable: Throwable): Boolean {
                    contentContainer.visibility = GONE
                    notFoundProductContainer.visibility = VISIBLE
                    buttonsGroup.visibility = GONE
                    setNotFoundProductListeners()
                    return false
                }
            })
        responseHandler.observe(
            this,
            viewModel.addResponse,
            object : Callback<Product> {
                override fun onLoaded(data: Product) {
                    startForResultStatusAfterSend.launch(
                        StatusAfterSendActivity.prepareIntent(
                            this@ProductDetailsActivity,
                            data.productCard?.barcode!!
                        )
                    )
                }
            })

        responseHandler.observe(this,
            viewModel.getResponse, object : Callback<Product> {
                override fun onLoaded(data: Product) {
                    setProductDetailsToEdit(data)
                    toolbar.setOnMenuItemClickListener {
                        startActivity(
                            Intent(
                                EditProductActivity.prepareIntent(
                                    this@ProductDetailsActivity,
                                    data,
                                    true
                                )
                            )
                        )
                        return@setOnMenuItemClickListener true
                    }
                }

                override fun onError(throwable: Throwable): Boolean {
                    contentContainer.visibility = GONE
                    errorScreen.visibility = VISIBLE
                    toolbar.menu.findItem(R.id.edit_product).isVisible = false
                    reloadButton.setOnClickListener {
                        viewModel.getRequest.value = intent.getStringExtra(UUID)
                    }
                    return false
                }
            })
    }

    private fun setProductDetailsToEdit(data: Product) {
        contentContainer.visibility = VISIBLE
        errorScreen.visibility = GONE
        buttonsGroup.visibility = GONE
        setupView(data)
    }

    private fun setupView(product: Product) {
        contentContainer.visibility = VISIBLE
        if (product.productCard?.photoUrl != null) {
            val glideUrl = GlideUrl(product.productCard?.photoUrl) {
                mapOf(
                    Pair(
                        "Authorization",
                        tokenUtil.getToken()
                    )
                )
            }
            Glide
                .with(this)
                .load(glideUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = GONE
                        return false
                    }
                })
                .into(image)
        } else {
            image.setImageResource(R.drawable.placeholder)
        }

        product.productCard?.apply {
            nameLabel.text = name
            brandLabel.text = brand
            addDataToHeader(
                categoryLabel,
                R.string.category_product_details,
                getString(Category.valueOf(category.toString()).category)
            )
            addDataToHeader(
                totalQuantityLabel,
                R.string.quantity,
                String.format(
                    "%s %s",
                    totalQuantity,
                    getString(MeasurementUnit.valueOf(measurementUnit.toString()).unit)
                )
            )
            if (price?.value != null && price?.currency != null) {
                addDataToHeader(
                    priceLabel,
                    R.string.price_product_details,
                    String.format("%s %s", price?.value, price?.currency)
                )
            }
            addSection(R.string.carbohydrates, nutriments?.carbohydrates.toString())
            addSection(R.string.energy, nutriments?.energy.toString())
            addSection(R.string.fat, nutriments?.fat.toString())
            addSection(R.string.fiber, nutriments?.fiber.toString())
            addSection(R.string.insatiableFat, nutriments?.insatiableFat.toString())
            addSection(R.string.salt, nutriments?.salt.toString())
            addSection(R.string.saturatedFat, nutriments?.saturatedFat.toString())
            addSection(R.string.sodium, nutriments?.sodium.toString())
            addSection(R.string.sugars, nutriments?.sugars.toString())
        }
        checkbox.isChecked = product.metadata?.shared == true
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDataPicker() {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        buttonSecond.setOnClickListener {
            val dtp =
                DatePickerDialog(
                    this,
                    { _, mYear, mMonth, mDay ->
                        viewModel.addRequest.value = viewModel.searchResponse.value?.data.apply {
                            this?.setMetadata(
                                ProductMetadata().setExpiryDate(
                                    dateFormatterUtils.format(mDay, mMonth, mYear)
                                ).setShared(checkbox.isChecked)
                            )
                        }
                    },
                    year,
                    month,
                    day
                )
            dtp.setCustomTitle(
                DatePickerCustomTitle(
                    this,
                    title = getString(R.string.set_execution_date)
                )
            )
            dtp.setCancelable(false)
            dtp.show()
        }
    }

    private fun setNotFoundProductListeners() {
        scanButtonAfterError.setOnClickListener {
            notFoundProductContainer.visibility = GONE
            setResult(Activity.RESULT_OK, Intent().putExtra(ACTION, SCAN))
            finish()
        }
        manualAddProductButton.setOnClickListener {
            startForResultAddProduct.launch(
                AddProductActivity.prepareIntent(
                    this,
                    intent.getStringExtra(BARCODE)!!
                )
            )
        }
    }

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            when (requestCode) {
                EDIT_PRODUCT -> {
                    detailsContainer.removeAllViews()
                    setupView(result.data?.getParcelableExtra(PRODUCT_DETAILS)!!)
                    viewModel.searchResponse.value.apply {
                        this?.data = (result.data!!.getParcelableExtra(PRODUCT_DETAILS)!!)
                    }
                }
                ACTION_AFTER_SEND -> {
                    setResult(Activity.RESULT_OK, result.data)
                    finish()
                }
            }
        }
    }

    private fun addDataToHeader(view: TextView, label: Int, value: String?) {
        if (value != null) {
            view.text = String.format(getString(label, value))
        } else view.visibility = GONE
    }

    private fun addSection(label: Int?, value: String?) {
        label?.let { l ->
            value?.let { v ->
                detailsContainer.apply {
                    addView(
                        ProductDetailsSection(this@ProductDetailsActivity)
                            .setLabel(getString(l))
                            .setValue(v)
                    )
                }
            }
        }
    }

    companion object {
        fun prepareIntent(context: Context, barcode: String?): Intent {
            return Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra(BARCODE, barcode)
                addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            }
        }

        fun prepareIntentWithUIID(context: Context, uuid: String): Intent {
            return Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra(UUID, uuid)
                addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            }
        }

        private const val UUID = "UUID"
        private const val BARCODE = "BARCODE"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val EDIT_PRODUCT = 269
        const val ACTION_AFTER_SEND = 43212
    }
}