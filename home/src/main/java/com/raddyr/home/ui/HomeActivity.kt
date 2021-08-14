package com.raddyr.home.ui

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.util.extensions.displayQuestionDialog
import com.raddyr.core.util.interfaces.Home
import com.raddyr.home.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity(override val contentViewLayout: Int = R.layout.home_activity) : BaseActivity(),
    Home {

    lateinit var navController: NavController

    override fun afterView() {
        toolbar.hideNavigationArrow()
        setBottomNavigationAndTitle()
    }


    private fun setBottomNavigationAndTitle() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.setToolbarTitle(
                destination.label.toString()
            )
        }
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun onBackPressed() {
        displayQuestionDialog(
            getString(R.string.are_you_sure_close),
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                finish()
            })
    }

    override fun getProducts() {
        navController.navigate(navController.graph.startDestination)
    }

    private fun getScan() {
        navController.navigate(navController.graph.elementAtOrNull(1)!!.id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.hasExtra(ACTION)) {
                    when (it.getStringExtra(ACTION)) {
                        PRODUCTS -> getProducts()
                        SCAN -> getScan()
                    }
                }
            }
        }
    }

    companion object {
        fun prepareIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }

        private const val ACTION = "ACTION"
        private const val PRODUCTS = "PRODUCTS"
        private const val SCAN = "SCAN"
    }
}