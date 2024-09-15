package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.Product
import com.example.storeapp.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val firestore : FirebaseFirestore)
    : ViewModel() {
              // Special Products
        private val _specialProducts = MutableSharedFlow<Resources<List<Product>>>()
        val specialProducts =_specialProducts as SharedFlow<Resources<List<Product>>>

    // Best Deals
    private val _bestDeals = MutableSharedFlow<Resources<List<Product>>>()
    val bestDeals =_bestDeals as SharedFlow<Resources<List<Product>>>


    // Best Products
    private val _bestProducts = MutableSharedFlow<Resources<List<Product>>>()
    val bestProducts =_bestProducts as SharedFlow<Resources<List<Product>>>


    init {
        getSpecialProducts()
        getBestDeals()
        getBestProducts()
    }
    fun getSpecialProducts() = viewModelScope.launch {
        firestore.collection("Products")
            .whereEqualTo("category","SpecialProduct")
            .get().addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Error(it.message.toString()))
                }
            }
    }



    fun getBestDeals() = viewModelScope.launch {
        firestore.collection("Products")
            .whereEqualTo("category","BestDeals")
            .get().addOnSuccessListener { result ->
                val bestDealsProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(Resources.Success(bestDealsProducts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resources.Error(it.message.toString()))
                }
            }
    }


    fun getBestProducts() = viewModelScope.launch {
        firestore.collection("Products")
            .whereEqualTo("category","BestProducts")
            .get().addOnSuccessListener { result ->
                val bestProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resources.Success(bestProducts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resources.Error(it.message.toString()))
                }
            }
    }

}

