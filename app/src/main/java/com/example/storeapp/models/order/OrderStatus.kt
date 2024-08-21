package com.example.storeapp.models.order

sealed class OrderStatus (
    val orderStatus : String
){
    object Ordered : OrderStatus("Ordered")
    object Canceled : OrderStatus("Canceled")
    object Confirmed : OrderStatus("Confirmed")
    object Shipped  : OrderStatus ("Shipped")
    object Delivered  :OrderStatus("Delivered")
    object Returned : OrderStatus("Returned")




}