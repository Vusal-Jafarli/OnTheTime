package com.example.onthetime.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.Employer
import com.example.onthetime.repository.AuthRepository
//import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor

//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlin.text.Typography.dagger

//import javax.inject.Inject

//@HiltViewModel
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    //    private var repository = AuthRepository()
    val signupStatus = MutableLiveData<Boolean>()
    val loginStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()


//    fun setRepository(repository: AuthRepository) {
//        this.repository = repository
//    }

    fun signUp(context:Context,employer: Employer) {

        repository.signUp(context,employer,
            onSuccess = { signupStatus.value = true },
            onFailure = { error -> errorMessage.value = error })


    }

    fun login( email: String, password: String): Boolean? {
        repository.login(email,
            password,
            onSucces = {
                loginStatus.value = true

            },
            onFailure = { loginStatus.value = false })

        return loginStatus.value
    }


}