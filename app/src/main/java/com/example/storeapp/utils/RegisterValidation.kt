package com.example.storeapp.utils

sealed class RegisterValidation {
    class Success : RegisterValidation()
    class Failed(val message : String)  : RegisterValidation()
}

 class RegisterFieldState(
    val email : RegisterValidation,
    val password : RegisterValidation
)