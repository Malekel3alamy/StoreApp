package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.order.Order
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth
) : ViewModel(){
    private val _order = MutableStateFlow<Resources<Order>>(Resources.UnSpecified())
    val order = _order.asStateFlow()



    fun placeOrder(order : Order){


        firestore.runBatch { batch ->
            // Add the order into user-orders collection
            val documentRef = firestore.collection("user").document(auth.uid!!).collection("order").document()

            batch.set(documentRef,order)
            // add the order into orders collection

            // delete product from user - cart collection
             firestore.collection("user").document(auth.uid!!).collection("cart")
                 .get().addOnSuccessListener {
                     it.documents.forEach {
                         it.reference.delete()
                     }
                 }
        }.addOnSuccessListener {
                viewModelScope.launch {
                    _order.emit(Resources.Success(order))
                }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resources.Error(it.message.toString()))
            }
        }
    }
}