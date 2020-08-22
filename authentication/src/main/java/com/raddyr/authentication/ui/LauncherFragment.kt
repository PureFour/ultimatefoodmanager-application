package com.raddyr.authentication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.raddyr.authentication.R
import com.raddyr.authentication.ui.AuthFragmentNavigator

class LauncherFragment : Fragment(R.layout.fragment_launcher) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AuthFragmentNavigator(this, view)
        super.onViewCreated(view, savedInstanceState)
    }
}