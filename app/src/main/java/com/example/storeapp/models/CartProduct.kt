package com.example.storeapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "productsTable"
)
data class CartProduct(
    @PrimaryKey private val id: Int?=null,
    val product: Product,
    private val productQuantity:Int,
    val colors:List<String>? = null

) {
    constructor() :this(null,Product(),1,null)
}