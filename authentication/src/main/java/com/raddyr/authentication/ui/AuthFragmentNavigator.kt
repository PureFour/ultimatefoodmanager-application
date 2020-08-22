package com.raddyr.authentication.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.raddyr.authentication.R
import kotlinx.android.synthetic.main.fragment_launcher.view.*

class AuthFragmentNavigator(fragment: Fragment, view: View) :
    View.OnClickListener {
    init {
        view.loginButton.setOnClickListener(this)
        view.registerButton.setOnClickListener(this)
    }

    private var navController: NavController = NavHostFragment.findNavController(fragment)

    override fun onClick(view: View?) {
        when (view?.id) {
//            R.id.loginButton -> navController.navigate(R.id.action_launcherFragment_to_loginFragment)
//            R.id.registerButton -> navController.navigate(R.id.action_launcherFragment_to_registerFragment)
        }
    }
}