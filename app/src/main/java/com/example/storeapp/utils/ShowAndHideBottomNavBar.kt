package com.example.storeapp.utils

import com.example.storeapp.R
import com.example.storeapp.ui.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

fun Fragment.hideNavView(){

    val bottomNavView = ( activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.shoppingBottomNavView)

    bottomNavView.visibility = View.GONE
}

fun Fragment.showNavView(){

    val bottomNavView = ( activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.shoppingBottomNavView)

    bottomNavView.visibility = View.VISIBLE
}