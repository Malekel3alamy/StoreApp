package com.example.storeapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductAddress(
    val addressTitle : String,
    val userFullName : String ,
    val street : String ,
    val phoneNumber : String ,
    val city : String ,
    val state : String
):Parcelable{
    constructor() :this("","","","","","")
}
