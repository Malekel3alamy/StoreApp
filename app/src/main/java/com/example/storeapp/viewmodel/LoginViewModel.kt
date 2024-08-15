package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaeseAuth : FirebaseAuth
) : ViewModel() {
    private  val _login = MutableSharedFlow<Resources<FirebaseUser>>()
    val login = _login.asSharedFlow()


    fun login (email:String , password:String) {
        firebaeseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            it.user?.let {
                viewModelScope.launch {
                    _login.emit(Resources.Success(it))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                it.message?.let {
                    _login.emit(Resources.Error(it))
                }

            }
        }
    }

    private val _resetPassword= MutableSharedFlow<Resources<String>>()
    val resetPassword = _resetPassword.asSharedFlow()
    fun resetPassword(email : String){

        viewModelScope.launch {
            firebaeseAuth.sendPasswordResetEmail(email).addOnSuccessListener {

            }.addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Error(" Failed To send Reset Password Email "))
                }
            }.addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Success(email))
                }
            }
        }
    }

}