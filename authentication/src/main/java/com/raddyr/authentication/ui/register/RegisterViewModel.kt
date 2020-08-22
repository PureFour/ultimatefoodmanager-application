package com.raddyr.authentication.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.authentication.data.model.AuthResponse
import com.raddyr.authentication.data.model.RegisterRequest
import com.raddyr.authentication.data.repository.AuthRepository
import com.raddyr.core.data.Resource
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    var registerRequest = MutableLiveData<RegisterRequest>()

    val response: LiveData<Resource<AuthResponse>> = Transformations.switchMap(registerRequest) {
        authRepository.register(it)
    }

    fun register(request: RegisterRequest): Pair<Boolean, Boolean> {
        val isAllInputFilled =
            request.email.isNotEmpty() && request.login.isNotEmpty() && request.password.isNotEmpty() && request.repeatPassword.isNotEmpty()
        val isPasswordsEquals = isPasswordsEquals(request.password, request.repeatPassword)
        if (isAllInputFilled && isPasswordsEquals) {
            registerRequest.value = request
        }
        return Pair(isAllInputFilled, isPasswordsEquals)
    }

    private fun isPasswordsEquals(pass1: String, pass2: String) = pass1 == pass2
}