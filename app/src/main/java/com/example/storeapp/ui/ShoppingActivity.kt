package com.example.storeapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.storeapp.R
import com.example.storeapp.databinding.ActivityShoppingBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    lateinit var binding: ActivityShoppingBinding
    val cartViewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {
                when(it){
                    is Resources.Error -> {

                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        val count = it.data?.size?:0
                        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.shoppingBottomNavView)
                        bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }

                    is Resources.UnSpecified -> Unit
                }
            }
        }


        val navHost = supportFragmentManager.findFragmentById(R.id.shoppingFragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.shoppingBottomNavView.setupWithNavController(navController)


    }
}