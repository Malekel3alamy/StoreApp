package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.CartProduct
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class DetailsViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
): ViewModel() {
private val _addToCart = MutableStateFlow<Resources<CartProduct>>(Resources.Loading())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct){
        firestore.collection("Products").document(firebaseAuth.uid.toString()).collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){// add new product

                    }else{
                        val product = it.first().toObject(CartProduct::class.java)

                        if (product == cartProduct){// increase product quantity

                        }else{ // add new product

                        }
                    }
                }
                viewModelScope.launch {

                }
            }.addOnFailureListener {

            }
    }

}