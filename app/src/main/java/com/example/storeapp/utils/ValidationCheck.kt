package com.example.storeapp.utils

import android.util.Patterns

fun validateEmail (email:String) : RegisterValidation{

    if(email.isEmpty()){
        return RegisterValidation.Failed("this Field can not be empty")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return return RegisterValidation.Failed("this pattern is not accepted ")
    }
    return RegisterValidation.Success()
}

fun validatePassword(password:String) :RegisterValidation{
    if(password.isEmpty()){
        return RegisterValidation.Failed("this Field can not be empty")
    }
    if(password.length <6){
        return RegisterValidation.Failed(" Password can not be less than 6 characters ")
    }
    return RegisterValidation.Success()
}
