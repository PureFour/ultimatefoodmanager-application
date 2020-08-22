package com.raddyr.authentication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.authentication.data.model.AuthResponse
import com.raddyr.authentication.data.model.LoginRequest
import com.raddyr.authentication.data.repository.AuthRepository
import com.raddyr.core.data.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    var loginRequest = MutableLiveData<LoginRequest>()
    val response: LiveData<Resource<AuthResponse>> = Transformations.switchMap(loginRequest) {
        authRepository.login(it)
    }

    fun login(request: LoginRequest): Boolean {
        val isAllInputFilled = request.email.isNotEmpty() && request.password.isNotEmpty()
        if (isAllInputFilled) {
            loginRequest.value = request
        }
        return isAllInputFilled
    }
}