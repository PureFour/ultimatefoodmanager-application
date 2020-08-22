package com.raddyr.ufm.config


import android.content.Context
import com.raddyr.authentication.ui.AuthActivity
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.home.ui.HomeActivity
import com.raddyr.products.ui.details.ProductDetailsActivity
import com.raddyr.scan.ui.share.ShareSettingsActivity
import java.util.*
import javax.inject.Inject

class UfmActivitiesAndFragmentConfig @Inject constructor() : ActivitiesAndFragmentConfig() {

    override fun authActivity(context: Context) = Optional.of(AuthActivity.prepareIntent(context))

    override fun homeActivity(context: Context) = Optional.of(HomeActivity.prepareIntent(context))

    override fun productDetailsActivity(context: Context, contents: String) =
        Optional.of(
            ProductDetailsActivity.prepareIntent(
                context,
                contents
            )
        )

    override fun shareSettingsActivity(context: Context, data: String) =
        Optional.of(ShareSettingsActivity.prepareIntent(context, data))
}