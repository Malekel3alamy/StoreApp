package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.firebase.FirebaseCommon
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
    val firebaseAuth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
private val _addToCart = MutableStateFlow<Resources<CartProduct>>(Resources.Loading())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct){
        firestore.collection("user").document(firebaseAuth.uid.toString()).collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){// add new product
                             addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(CartProduct::class.java)

                        if (product == cartProduct){// increase product quantity
                                increaseQuantity(it.first().id,cartProduct)
                        }else{ // add new product
                            addNewProduct(cartProduct)
                        }
                    }
                }

            }.addOnFailureListener {
                  viewModelScope.launch {
                      _addToCart.emit(Resources.Error(it.message.toString()))
                  }
            }
    }


    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToCart(cartProduct){addedProduct,exception ->
            viewModelScope.launch {
                if (exception == null){
                    _addToCart.emit(Resources.Success(addedProduct!!))
                }else{
                    _addToCart.emit(Resources.Error(exception.message.toString()))
                }
            }

        }
    }

    private fun increaseQuantity(documentId  :String ,cartProduct: CartProduct){

        firebaseCommon.increaseQuantity(documentId){ userId,e ->
            if (e == null){
                viewModelScope.launch {
                    _addToCart.emit(Resources.Success(cartProduct))
                }
            }else{
                viewModelScope.launch {
                    _addToCart.emit(Resources.Error(e.message.toString()))
                }
            }
        }
    }

}