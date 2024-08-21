package com.example.storeapp.utils



fun CalculatePriceAfterDiscount(price:Float,offer:Float?) : Float {

    if (offer!= null ){
        val disount = price *offer
        val newPrice = price - disount
        return newPrice
    }else{
        return price
    }

}