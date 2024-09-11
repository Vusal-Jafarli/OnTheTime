package com.example.onthetime.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.User
import com.example.onthetime.repository.AuthRepository
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlin.text.Typography.dagger

//import javax.inject.Inject

//@HiltViewModel
class AuthViewModel (private val repository: AuthRepository) : ViewModel() {
    //    private var repository = AuthRepository()
    val signupStatus = MutableLiveData<Boolean>()
    val loginStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()


//    fun setRepository(repository: AuthRepository) {
//        this.repository = repository
//    }

    fun signUp(user: User) {

        repository.signUp(user,
            onSuccess = { signupStatus.value = true },
            onFailure = { error -> errorMessage.value = error })


    }

    fun login(email: String, password: String) {
        repository.login(email,
            password,
            onSucces = { loginStatus.value = true },
            onFailure = { loginStatus.value = false })
    }


}