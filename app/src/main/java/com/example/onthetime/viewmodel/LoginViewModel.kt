package com.example.onthetime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean> get() = _navigateToSignUp



    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    fun onSignUpClicked() {
        _navigateToSignUp.value = true
    }

    fun onNavigateCompleted() {
        _navigateToLogin.value = false
        _navigateToSignUp.value = false
    }


}