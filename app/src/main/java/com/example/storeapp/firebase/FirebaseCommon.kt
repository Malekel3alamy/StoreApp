package com.example.storeapp.firebase

import com.example.storeapp.models.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth :FirebaseAuth,

) {
    private  val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addProductToCart(cartProduct: CartProduct,onResult : (CartProduct?,Exception?) -> Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)
            }.addOnFailureListener {
                onResult(null,it)
            }
    }
    fun increaseQuantity(documentId  : String ,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.productQuantity +1
                val newProductObject = cartProduct.copy(productQuantity = newQuantity)
                transaction.set(documentRef,newProductObject)
            }

        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    fun decreaseQuantity(documentId  : String ,onResult: (String?, Exception?) -> Unit){
        var newQuantity = 0
        firestore.runTransaction {transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                 newQuantity = cartProduct.productQuantity -1
                if (newQuantity!= 0 || newQuantity != -1){
                    val newProductObject = cartProduct.copy(productQuantity = newQuantity)
                    transaction.set(documentRef,newProductObject)
                }else{
                    // if product quantity became 0 or -1 it would be deleted from our cart collection
                    transaction.delete(documentRef)
                    newQuantity =0
                }

            }

        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
enum class QuantityChanging{
    INCREASE,DECREASE
}

}