package com.raddyr.products.ui.base

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.config.AppBuildConfig
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.products.R
import com.raddyr.products.data.model.*
import com.raddyr.products.data.model.Currency
import com.raddyr.products.ui.customViews.ProductInput
import com.raddyr.products.ui.customViews.ProductSpinner
import com.raddyr.products.ui.customViews.addProductInput
import com.raddyr.products.ui.customViews.addProductSpinner
import com.raddyr.products.ui.utils.DatePickerCustomTitle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_product_activity.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.*
import java.util.*
import javax.inject.Inject

abstract class BaseProductActivity<T : BaseProductViewModel>() :
    BaseActivity() {

    lateinit var name: ProductInput
    lateinit var brand: ProductInput
    lateinit var price: ProductInput
    lateinit var totalQuantity: ProductInput
    lateinit var barcode: ProductInput
    lateinit var measurementUnit: ProductSpinner
    lateinit var category: ProductSpinner
    lateinit var currency: ProductSpinner
    lateinit var carbohydrates: ProductInput
    lateinit var energy: ProductInput
    lateinit var fat: ProductInput
    lateinit var fiber: ProductInput
    lateinit var insatiableFat: ProductInput
    lateinit var salt: ProductInput
    lateinit var saturatedFat: ProductInput
    lateinit var sodium: ProductInput
    lateinit var sugars: ProductInput

    lateinit var requestData: Product

    @Inject
    lateinit var appBuildConfig: AppBuildConfig

    @Inject
    lateinit var dataCache: DataCache

    @Inject
    lateinit var dateFormatterUtils: DateFormatterUtils

    lateinit var viewModel: T

    var imageFile: File? = null
    var productToEdit: Product? = null
    private val startForResultCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(CAMERA_REQUEST_CODE, result)
    }

    override fun afterView() {
        setViewModel()
        setViews()
        setObserver()
        setSharedCheckboxVisibility()
        setPhotoButton()
        setDataPicker()
    }

    abstract fun setViewModel()

    fun invokeImageUploadRequest() {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile!!)
        viewModel.uploadImageRequest.value =
            MultipartBody.Part.createFormData("imageFile", imageFile!!.name, reqFile)
    }

    abstract fun invokeRequest(product: Product)

    private fun setObserver() {
        responseHandler.observe(
            this,
            viewModel.uploadResponse,
            object : Callback<ImageUploadResponse> {
                override fun onLoaded(data: ImageUploadResponse) {
                    if (intent.getBooleanExtra(IS_TO_EDIT, false)) {
                        invokeRequest(requestData.apply {
                            productCard?.setPhotoUrl(
                                getImageUri(
                                ) + data.uuid
                            )
                        })
                    } else {
                        customCallback(data.uuid)
                    }
                }
            })
    }

    open fun customCallback(uuid: String) {
        invokeRequest(requestData.apply {
            productCard?.setPhotoUrl(
                getImageUri(
                ) + uuid
            )
        })
    }

    private fun setDataPicker() {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        button.setOnClickListener {
            if (!barcode.getValue().isNullOrEmpty() && !name.getValue().isNullOrEmpty()) {
                val dtp = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                        setDateInRequest(dateFormatterUtils.format(mDay, mMonth, mYear))
                        invokeImageUploadRequestOrProductRequest()
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
            } else {
                dialogManager.showSnackbar(R.string.barcode_and_name_required)
            }
        }
    }

    private fun invokeImageUploadRequestOrProductRequest() {
        if (imageFile != null) {
            invokeImageUploadRequest()
        } else {
            invokeRequest(requestData)
        }
    }

    private fun setDateInRequest(date: String) {
        requestData = getDataFromViews().apply {
            this.metadata?.expiryDate = date
        }
    }

    private fun setPhotoButton() {
        addPhotoButton.setOnClickListener {
            if (checkAndRequestPermission()) {
                onPermissionGranted()
            }
        }
    }

    private fun onPermissionGranted() {
        openCamera()
    }

    private fun openCamera() {
        imageFile = getPhotoFile()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            val fileProvider = FileProvider.getUriForFile(
                this@BaseProductActivity,
                PACKAGE_PROVIDER,
                imageFile!!
            )
            putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startForResultCamera.launch(intent)

        } else {
            Toast.makeText(this, R.string.cannot_open_camera, Toast.LENGTH_LONG).show()
        }
    }

    private fun setViews() {
        productDetailsContainer.apply {

            with(productToEdit?.productCard) {
                barcode = addProductInput(
                    getString(R.string.barcode), this?.barcode, isDisabled = true
                )
                name = addProductInput(
                    getString(R.string.name),
                    this?.name
                )
                brand = addProductInput(
                    getString(R.string.brand),
                    this?.brand
                )

                category = addProductSpinner(
                    ProductSpinner(
                        this@BaseProductActivity,
                        getString(R.string.category_product_edit),
                        Category.getDefaultCategoriesList(),
                        this?.category
                    )
                )
                measurementUnit = addProductSpinner(
                    ProductSpinner(
                        this@BaseProductActivity,
                        getString(R.string.measurementUnit),
                        MeasurementUnit.getDefaultCategoriesList(),
                        this?.measurementUnit
                    )
                )

                price = addProductInput(
                    getString(R.string.price_product_edit),
                    this?.price?.value?.toString(),
                    isNumberInput = true
                )
                currency = addProductSpinner(
                    ProductSpinner(
                        this@BaseProductActivity,
                        getString(R.string.currency),
                        Currency.getDefaultCurrenciesList()
                    )
                )
                totalQuantity = addProductInput(
                    getString(R.string.totalQuantity),
                    this?.totalQuantity?.toString(),
                    isNumberInput = true
                )

                carbohydrates = addProductInput(
                    getString(R.string.carbohydrates),
                    this?.nutriments?.carbohydrates?.toString(),
                    isNumberInput = true
                )
                energy = addProductInput(
                    getString(R.string.energy),
                    this?.nutriments?.energy?.toString(),
                    isNumberInput = true
                )
                fat = addProductInput(
                    getString(R.string.fat),
                    this?.nutriments?.fat?.toString(),
                    isNumberInput = true
                )
                fiber = addProductInput(
                    getString(R.string.fiber),
                    this?.nutriments?.fiber?.toString(),
                    isNumberInput = true
                )

                insatiableFat = addProductInput(
                    getString(R.string.insatiableFat),
                    this?.nutriments?.insatiableFat?.toString(),
                    isNumberInput = true
                )
                salt = addProductInput(
                    getString(R.string.salt),
                    this?.nutriments?.salt?.toString(),
                    isNumberInput = true
                )
                saturatedFat = addProductInput(
                    getString(R.string.saturatedFat),
                    this?.nutriments?.saturatedFat?.toString(),
                    isNumberInput = true
                )
                sodium = addProductInput(
                    getString(R.string.sodium),
                    this?.nutriments?.sodium?.toString(),
                    isNumberInput = true
                )
                sugars = addProductInput(
                    getString(R.string.sugars),
                    this?.nutriments?.sugars?.toString(),
                    isNumberInput = true
                )
            }
        }
    }

    fun getDataFromViews(): Product =
        Product().setProductCard(
            ProductCard().setName(name.getValue()).setBarcode(barcode.getValue())
                .setBrand(brand.getValue())
                .setCategory(category.getValue() as Category)
                .setPrice(Price(price.getValue()?.toFloat(), currency.getValue().toString()))
                .setMeasurementUnit(measurementUnit.getValue() as MeasurementUnit)
                .setTotalQuantity(totalQuantity.getValue()?.toFloat())
                .setPhotoUrl(getPhotoUrl())
                .setNutriments(
                    Nutriments().setCarbohydrates(
                        carbohydrates.getValue()?.toFloat()
                    ).setEnergy(
                        energy.getValue()?.toFloat()
                    )
                        .setFiber(fiber.getValue()?.toFloat())
                        .setInsatiableFat(
                            insatiableFat.getValue()?.toFloat()
                        )
                        .setSalt(salt.getValue()?.toFloat())
                        .setSaturatedFat(
                            saturatedFat.getValue()?.toFloat()
                        )
                        .setFat(fat.getValue()?.toFloat())
                        .setSodium(
                            sodium.getValue()?.toFloat()
                        )
                        .setSugars(
                            sugars.getValue()?.toFloat()
                        )
                )
        ).setQuantity(totalQuantity.getValue()?.toFloat())
            .setMetadata(ProductMetadata().setShared(checkbox.isChecked))

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (requestCode == CAMERA_REQUEST_CODE && result.resultCode == Activity.RESULT_OK) {
            val ei = ExifInterface(imageFile?.absolutePath!!)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            var rotatedBitmap: Bitmap? = null
            when(orientation) {
                ExifInterface.ORIENTATION_ROTATE_90-> rotatedBitmap = rotateImage(BitmapFactory.decodeFile(imageFile?.absolutePath), 90f)
                ExifInterface.ORIENTATION_ROTATE_180-> rotatedBitmap = rotateImage(BitmapFactory.decodeFile(imageFile?.absolutePath), 180f)
                ExifInterface.ORIENTATION_ROTATE_270-> rotatedBitmap = rotateImage(BitmapFactory.decodeFile(imageFile?.absolutePath), 270f)
                ExifInterface.ORIENTATION_NORMAL-> rotatedBitmap = BitmapFactory.decodeFile(imageFile?.absolutePath)
                ExifInterface.ORIENTATION_UNDEFINED-> rotatedBitmap = BitmapFactory.decodeFile(imageFile?.absolutePath)
            }
            image.setImageBitmap(rotatedBitmap)
            dialogManager.showProgressBar()
            subscriptionManager.register(
                Single.fromCallable { compressImage(rotatedBitmap!!) }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ f ->
                        image.apply {
                            visibility = View.VISIBLE
                        }
                        dialogManager.hideProgressBar()
                    },
                        { Timber.d("error") })
            )
        }
    }

    private fun compressImage(bitmap: Bitmap) {
        imageFile = convertDataToByte(bitmap)
    }

    private fun convertDataToByte(imageBitmap: Bitmap): File {
        val f = File(this.cacheDir, "${barcode.getValue()}")
        f.createNewFile()
        val bos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
        val bitmapData = bos.toByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitmapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when {
            (requestCode == PERMISSION_REQUEST_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) -> onPermissionGranted()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkAndRequestPermission(): Boolean {
        return if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
            false
        }
    }

    private fun getPhotoUrl(): String? {
        return if (intent.hasExtra(PRODUCT_DETAILS)) {
            intent.getParcelableExtra<Product>(PRODUCT_DETAILS)?.productCard?.photoUrl
        } else null
    }

    private fun getPhotoFile(): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(FILE_NAME, ".jpg", storageDirectory)
    }

    private fun setSharedCheckboxVisibility() {
        checkboxGroup.isVisible = dataCache.isProductsShared(this)
    }

    fun getImageUri() =
        appBuildConfig.SERVER_URL() + IMAGE_QUERY

    companion object {
        const val IS_TO_EDIT = "IS_TO_EDIT"
        const val CAMERA_REQUEST_CODE = 101
        const val PERMISSION_REQUEST_CODE = 123
        const val PRODUCT_DETAILS = "PRODUCT_DETAILS"
        private const val FILE_NAME = "photo.jpg"
        private const val IMAGE_QUERY = "8081/images?uuid="
        private const val PACKAGE_PROVIDER = "com.raddyr.ufm.fileprovider"

        fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        }
    }
}