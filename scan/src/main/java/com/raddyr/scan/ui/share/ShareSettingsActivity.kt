package com.raddyr.scan.ui.share

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.util.extensions.displayInfoDialog
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.scan.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.share_settings_activity.*
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ShareSettingsActivity(override val contentViewLayout: Int = R.layout.share_settings_activity) :
    BaseActivity() {

    @Inject
    lateinit var sharedInfoService: SharedInfoService

    @Inject
    lateinit var dataCache: DataCache

    private val viewModel by lazy { viewModelFactory.create(ShareSettingsViewModel::class.java) }

    override fun afterView() {
        if (intent.hasExtra(SHARE_UUID)) {
            viewModel.shareRequest.value = intent.getStringExtra(SHARE_UUID)
        } else {
            updateSharedInfo()
        }
        setListeners()
        setProductsSharing()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setObservers()
    }

    private fun setListeners() {
        generateQrButton.setOnClickListener {
            viewModel.uuidRequest.value = true
        }
    }

    private fun setObservers() {
        responseHandler.observe(this, viewModel.uuidResponse, object : Callback<String> {
            override fun onLoaded(data: String) {
                try {
                    val barcodeEncoder = BarcodeEncoder()
                    val bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400)
                    displayInfoDialog(
                        title = "QR Code",
                        view = ImageView(this@ShareSettingsActivity).apply { setImageBitmap(bitmap) },
                        listener = DialogInterface.OnClickListener { _, _ ->
                            updateSharedInfo()
                        },
                        cancelable = false
                    )
                } catch (e: Exception) {
                    Timber.d(e)
                }
            }
        })

        responseHandler.observe(
            this,
            viewModel.shareResponse,
            object : Callback<Unit> {
                override fun onLoaded(data: Unit) {
                    Toast.makeText(
                        this@ShareSettingsActivity,
                        getString(R.string.shared_success),
                        Toast.LENGTH_LONG
                    ).show()
                    updateSharedInfo()
                }
            })
    }

    private fun setProductsSharing() {
        val usrs = sharedPreferences.getStringListPreferences(
            this@ShareSettingsActivity,
            USER_INFO
        ).takeIf { it?.isNotEmpty()!! }?.reduce { acc, s -> "$acc, $s" }
        if (usrs.isNullOrEmpty()) {
            productSharedValue.text = getString(R.string.text_no)
            peopleProductsGroup.visibility = View.GONE
        } else {
            productSharedValue.text = getString(R.string.text_yes)
            peopleProductsGroup.visibility = View.VISIBLE
            peopleValue.text = usrs

        }
    }

    private fun updateSharedInfo() {
        dialogManager.showProgressBar()
        sharedInfoService.fetchData(this) {
            setProductsSharing()
            dialogManager.hideProgressBar()
        }
    }

    companion object {
        const val USER_INFO = "USER_INFO"
        private const val SHARE_UUID = "SHARE_UUID"

        fun prepareIntent(context: Context, data: String): Intent =
            Intent(context, ShareSettingsActivity::class.java).apply {
                putExtra(SHARE_UUID, data)
            }
    }
}