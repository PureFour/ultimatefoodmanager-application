package com.raddyr.core.config

import android.content.Context

abstract class SettingsConfig {
    open fun settingsList(context: Context): List<Pair<String, () -> Unit>> = emptyList()
}