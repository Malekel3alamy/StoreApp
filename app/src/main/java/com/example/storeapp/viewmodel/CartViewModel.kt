package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.CartProduct
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private  val firestore: FirebaseFirestore,
                    private val auth:FirebaseAuth
) : ViewModel() {


    private val _cartProducts = MutableSharedFlow<Resources<List<CartProduct>>>()
    val cartProducts = _cartProducts as SharedFlow<Resources<List<CartProduct>>>

    fun makeCheckOut() {

    }

    fun increaseQuantity(){

    }

    fun getCartProducts(){

             firestore.collection("user").document(auth.uid!!)
                 .collection("cart").get().addOnSuccessListener {results ->
                     val cartProducts = results.toObjects(CartProduct::class.java)

                     viewModelScope.launch {

                         _cartProducts.emit(Resources.Success(cartProducts))
                     }
                 }.addOnFailureListener {
                     viewModelScope.launch {
                         _cartProducts.emit(Resources.Error(it.message.toString()))
                     }
                 }
    }

}