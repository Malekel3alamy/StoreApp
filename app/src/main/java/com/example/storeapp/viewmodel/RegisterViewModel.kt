package com.example.storeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.User
import com.example.storeapp.utils.Contants.Companion.USER_COLLECTION
import com.example.storeapp.utils.RegisterFieldState
import com.example.storeapp.utils.RegisterValidation
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.validateEmail
import com.example.storeapp.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor
    (private val firebaseAuth:FirebaseAuth
            ,private val db:FirebaseFirestore) : ViewModel () {

        private val _register = MutableStateFlow<Resources<User>>(Resources.Loading())
        val  register : Flow<Resources<User>> = _register
        private val _validation = Channel<RegisterFieldState>()
    val validation  = _validation.receiveAsFlow()



        fun createAnAccountWithEmailAnPassword(user: User,password:String) {
            if (checkValidation(user, password)) {
                firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                    .addOnSuccessListener {
                        it.user?.let {
                            saveUserInfo(it.uid,user)
                            _register.value = Resources.Success(user)
                        }
                    }
                    .addOnFailureListener {
                        _register.value = Resources.Error(it.message.toString())
                        Log.d("RegisterFragment"," Failed To Save ")
                    }
            }else{
                val registerFieldState = RegisterFieldState(
                    validateEmail(user.email), validatePassword(password)
                )
                viewModelScope.launch {
                    _validation.send(registerFieldState)
                }
            }
        }



    private fun saveUserInfo(userUid: String, user: User) {
               db.collection(USER_COLLECTION)
                   .document(userUid).
                   set(user).addOnSuccessListener {
                       Log.d("RegisterFragment"," succeeded  To Save ")

                   }.addOnFailureListener {
                      // _register.value = Resources.Error(it.message.toString())
                       Log.d("RegisterFragment"," Failed To Save ")
                   }
    }

    private fun checkValidation(user: User, password: String) :Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldregister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldregister
    }
}