package com.raddyr.scan.ui

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class CaptureActivityPortrait : CaptureActivity() {

    companion object {
        fun initiateScanForFragment(fragment: Fragment) {
            val integrator = IntentIntegrator.forSupportFragment(fragment)
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)
            integrator.setOrientationLocked(true)
            integrator.setBeepEnabled(false)
            integrator.captureActivity = CaptureActivityPortrait::class.java
            integrator.initiateScan()
        }

        fun initiateScanForActivity(activity: Activity) {
            val integrator = IntentIntegrator(activity)
            integrator.setPrompt("Scan a barcode")
            integrator.setCameraId(0)
            integrator.setOrientationLocked(true)
            integrator.setBeepEnabled(false)
            integrator.captureActivity = CaptureActivityPortrait::class.java
            integrator.initiateScan()
        }
    }
}