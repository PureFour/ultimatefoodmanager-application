package com.raddyr.core.config

import android.content.Context
import android.content.Intent
import java.util.*

open class ActivitiesAndFragmentConfig {

    open fun authActivity(context: Context): Optional<Intent> = Optional.empty()

    open fun homeActivity(context: Context): Optional<Intent> = Optional.empty()

    open fun productDetailsActivity(context: Context, contents: String): Optional<Intent> = Optional.empty()

    open fun shareSettingsActivity(context: Context, data: String): Optional<Intent> = Optional.empty()
}