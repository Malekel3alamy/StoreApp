package com.example.storeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.firebase.FirebaseCommon
import com.example.storeapp.models.CartProduct
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private  val firestore: FirebaseFirestore,
                    private val auth:FirebaseAuth,
                    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    init {
        getCartProducts()
    }
    private val _cartProducts = MutableStateFlow<Resources<List<CartProduct>>>(Resources.Loading())
    val cartProducts = _cartProducts.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()


    private var cartProductsDocuments = emptyList<DocumentSnapshot>()

    fun makeCheckOut() {

    }

    fun getCartProducts(){

             firestore.collection("user").document(auth.uid!!)
                 .collection("cart").addSnapshotListener { value, error ->
                     if (error != null || value == null){
                         viewModelScope.launch {
                             _cartProducts.emit(Resources.Error(error?.message.toString()))
                         }
                     }else{
                         val cartProducts = value.toObjects(CartProduct::class.java)
                         cartProductsDocuments = value.documents
                         viewModelScope.launch {
                             cartProducts.let {
                                 _cartProducts.emit(Resources.Success(it))
                             }
                         }

                     }

                 }
    }


fun deleteCarTProduct(cartProduct: CartProduct){
    val index = cartProducts.value.data?.indexOf(cartProduct)
    if (index != null && index != -1 ){
        Log.d("DELETEPRODUCT"," Deleted ")
        val documentId = cartProductsDocuments[index].id
        firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId).delete()
    }

}
    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){


       val index = cartProducts.value.data?.indexOf(cartProduct)
        /**
         * Index could be equal to -1 if the function [getCartProducts] delays which will also delay the result we expect to be inside the [_cartProducts]
         * and to prevent the app from crashing we make a check
         */
        if (index != null&& index != -1 ){
            val documentId = cartProductsDocuments[index].id


            when (quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch {
                        _cartProducts.emit(Resources.Loading())
                    }
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {

                    if (cartProduct.productQuantity == 1) {
                        Log.d("Index1", index.toString())
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }
                    } else {

                        viewModelScope.launch {
                            _cartProducts.emit(Resources.Loading())
                        }
                        decreaseQuantity(documentId)
                    }
                }
                }
            }

        }



    private fun increaseQuantity(documentId  : String){

        firebaseCommon.increaseQuantity(documentId ){documentId,e ->
            if (e==null){

            }else{
                viewModelScope.launch {
                    _cartProducts.emit(Resources.Error(e.message.toString()))
                }
            }

        }
    }

    private  fun decreaseQuantity(documentId  : String){
        firebaseCommon.decreaseQuantity(documentId ){documentId,e ->
            if (e !=null){
                viewModelScope.launch {
                    _cartProducts.emit(Resources.Error(e?.message.toString()))
                }
            }
        }
    }

}