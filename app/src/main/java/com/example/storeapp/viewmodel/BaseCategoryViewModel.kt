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
class BaseCategoryViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel() {


    private val _chairs = MutableSharedFlow<Resources<List<Product>>>()
    val chairs = _chairs as SharedFlow<Resources<List<Product>>>

    private val _tables = MutableSharedFlow<Resources<List<Product>>>()
    val tables = _tables as SharedFlow<Resources<List<Product>>>

    private val _cupBoards = MutableSharedFlow<Resources<List<Product>>>()
    val cupBoards = _cupBoards as SharedFlow<Resources<List<Product>>>

    private val _accessories = MutableSharedFlow<Resources<List<Product>>>()
    val accessories = _accessories as SharedFlow<Resources<List<Product>>>

    private val _furniture = MutableSharedFlow<Resources<List<Product>>>()
    val furniture = _furniture as SharedFlow<Resources<List<Product>>>

    // Chairs
    fun getChairs() = getData("Chair",_chairs)

    // Tables
    fun getTables() = getData("Table",_tables)

    // CupBoard
    fun getCupBoard() = getData("CupBoard",_cupBoards)

    // Accessories
    fun getAccessories()=getData("Accessory",_accessories)


    // Furniture
    fun getFurniture() = getData("Furniture",_furniture)




    private fun getData(category:String,sharedFlow: MutableSharedFlow<Resources<List<Product>>>)=viewModelScope.launch {
        firestore.collection("Products")
            .whereEqualTo("category","$category")
            .get().addOnSuccessListener { result ->
                val productsData = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    sharedFlow.emit(Resources.Success(productsData))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    sharedFlow.emit(Resources.Error(it.message.toString()))
                }
            }
    }


}