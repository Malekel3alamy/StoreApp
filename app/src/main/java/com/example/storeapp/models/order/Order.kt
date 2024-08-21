package com.example.storeapp.models.order

import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.ProductAddress

data class Order(
   val orderStatus  :String ,
   val totalPrice : Float ,
    val products : List<CartProduct>,
    val address :ProductAddress
)
