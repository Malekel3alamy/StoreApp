package com.example.storeapp.models.order

import android.location.Address
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.ProductAddress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

data class Order(
    val orderStatus  :String = "",
    val totalPrice : Float =0f,
    val products : List<CartProduct> = emptyList(),
    val address :ProductAddress = ProductAddress(),
    val orderId:Long = nextLong(0,100_000_000)+totalPrice.toLong(),
    val date:String =SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(Date())
)
