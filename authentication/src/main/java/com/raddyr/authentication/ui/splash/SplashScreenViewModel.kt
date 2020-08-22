package com.raddyr.authentication.ui.splash

import androidx.lifecycle.ViewModel
import com.raddyr.authentication.data.repository.AuthRepository
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val response = authRepository.checkServerAvailable()
}