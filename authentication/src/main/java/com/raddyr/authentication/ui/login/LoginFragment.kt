package com.raddyr.authentication.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raddyr.authentication.R
import com.raddyr.authentication.data.model.AuthResponse
import com.raddyr.authentication.data.model.LoginRequest
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.util.ErrorHandler
import com.raddyr.core.util.extensions.displaySnackbar
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.tokenUtil.TokenUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment(override val contentViewLayout: Int = R.layout.fragment_login) :
    BaseFragment() {

    @Inject
    lateinit var responseHandler: ResponseHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenUtil: TokenUtil

    @Inject
    lateinit var activitiesAndFragmentConfig: ActivitiesAndFragmentConfig

    @Inject
    lateinit var sharedInfoService: SharedInfoService

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun afterView() {
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        loginButton.setOnClickListener { login() }
        registerButton.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }
    }

    private fun login() {
        val loginRequest = LoginRequest(emailInput.getText(), passwordInput.getText())
        if (!viewModel.login(loginRequest)) {
            displaySnackbar(getString(R.string.input_not_filled))
        }
    }

    private fun setObservers() {
        responseHandler.observe(
            viewLifecycleOwner,
            viewModel.response,
            object : Callback<AuthResponse> {
                override fun onLoaded(data: AuthResponse) {
                    handleLoginResponse(data)
                    sharedInfoService.fetchData(
                        requireActivity() as AppCompatActivity
                    ) { redirectToHome() }
                }

                override fun onError(throwable: Throwable): Boolean {
                    return if (throwable is HttpException) {
                        dialogManager.showSnackbar(
                            when (throwable.code()) {
                                ErrorHandler.NOT_FOUND -> R.string.user_not_found
                                ErrorHandler.AUTH_ERROR -> R.string.password_not_correct
                                else -> R.string.something_wrong
                            }
                        )
                        false
                    } else true

                }
            })
    }

    private fun handleLoginResponse(data: AuthResponse) {
        tokenUtil.setToken(data.token!!)
    }

    private fun redirectToHome() {
        startActivity(activitiesAndFragmentConfig.homeActivity(requireContext()).get())
        activity?.finish()
    }
}