package com.raddyr.authentication.ui.register


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raddyr.authentication.R
import com.raddyr.authentication.data.model.AuthResponse
import com.raddyr.authentication.data.model.RegisterRequest
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.config.ActivitiesAndFragmentConfig
import com.raddyr.core.push.FirebaseMessagingManager
import com.raddyr.core.util.extensions.displaySnackbar
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.tokenUtil.TokenUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment(override val contentViewLayout: Int = R.layout.fragment_register) :
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
    lateinit var firebaseMessagingManager: FirebaseMessagingManager

    @Inject
    lateinit var sharedInfoService: SharedInfoService


    private val viewModel by viewModels<RegisterViewModel> { viewModelFactory }

    override fun afterView() {
        setListeners()
        setLoginObserver()
    }

    private fun setListeners() {
        registerButton.setOnClickListener { register() }
        loginButton.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }

    }

    private fun register() {
        firebaseMessagingManager.getToken(
            callback = object : FirebaseMessagingManager.MessagingManagerCallback {
                override fun onSuccess(token: String?) {
                    val registerRequest = RegisterRequest(
                        emailInput.getText(),
                        loginInput.getText(),
                        passwordInput.getText(),
                        repeatPasswordInput.getText(),
                        token
                    )
                    val errorPair = viewModel.register(registerRequest)
                    if (!errorPair.first) {
                        displaySnackbar(getString(R.string.input_not_filled))
                    } else if (!errorPair.second) {
                        displaySnackbar(getString(R.string.password_must_be_equals))
                    }
                }

                override fun onFailure(exception: Throwable?) {
                    dialogManager.showSnackbar()
                    Timber.d(exception)
                }
            }
        )
    }

    private fun setLoginObserver() {
        responseHandler.observe(
            viewLifecycleOwner,
            viewModel.response,
            object : Callback<AuthResponse> {
                override fun onLoaded(data: AuthResponse) {
                    handleResponse(data)
                }
            })
    }

    private fun handleResponse(data: AuthResponse) {
        tokenUtil.setToken(data.token!!)
        sharedInfoService.fetchData(requireActivity() as AppCompatActivity) {
            startActivity(activitiesAndFragmentConfig.homeActivity(requireContext()).get())
            activity?.finish()
        }
    }
}