package com.raddyr.ufm.config

import android.content.Context
import android.content.Intent
import com.raddyr.core.config.SettingsConfig
import com.raddyr.settings.ui.profile.ProfileSettingsActivity
import com.raddyr.scan.ui.share.ShareSettingsActivity
import com.raddyr.ufm.R
import javax.inject.Inject

class UfmSettingsConfig @Inject constructor() : SettingsConfig() {
    override fun settingsList(context: Context): List<Pair<String, () -> Unit>> =
        mutableListOf(
            Pair(
                context.getString(R.string.profil),
                { context.startActivity(Intent(context, ProfileSettingsActivity::class.java)) }),
            Pair(
                context.getString(R.string.product_sharing),
                { context.startActivity(Intent(context, ShareSettingsActivity::class.java)) })
        )
}