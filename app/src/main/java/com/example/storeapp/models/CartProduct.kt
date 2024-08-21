package com.example.storeapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


/*
@Entity(
    tableName = "productsTable"
)*/
@Parcelize
data class CartProduct(
    //@PrimaryKey private val id: Int?=null,
    val product: Product,
    val productQuantity:Int,
    val colors:List<String>? = null

):Parcelable {
    constructor() :this(Product(),1,null)
}