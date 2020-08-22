package com.raddyr.settings.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.config.SettingsConfig
import com.raddyr.settings.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.settings_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment(override val contentViewLayout: Int = R.layout.settings_fragment) :
    BaseFragment() {

    @Inject
    lateinit var settingsConfig: SettingsConfig

    override fun afterView() {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = SettingsAdapter(settingsConfig.settingsList(requireContext()))
    }
}