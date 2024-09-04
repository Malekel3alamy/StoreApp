package com.example.storeapp.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.ProductAddress
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth
):ViewModel() {

 private val _addNewAddress = MutableStateFlow<Resources<ProductAddress>>(Resources.Loading())
    val addNewAddress = _addNewAddress.asStateFlow()



    private val _inputError = MutableSharedFlow<String>()
    val inputError = _inputError.asSharedFlow()

    fun addAddress(address:ProductAddress){
        val validateInputs = validateInputs(address)

           if (validateInputs){
               firestore.collection("user").document(auth.uid!!).collection("address")
                   .document().set(address).addOnSuccessListener {
                       viewModelScope.launch {
                           _addNewAddress.emit(Resources.Success(address))
                       }

                   }.addOnFailureListener {
                       viewModelScope.launch {
                           _addNewAddress.emit(Resources.Error(it.message.toString()))
                       }
                   }
           }else{

               viewModelScope.launch {
                   _inputError.emit(" All fields Are Required ")
               }
           }


    }

    private fun validateInputs(address: ProductAddress): Boolean {

        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty()&&
                address.street.trim().isNotEmpty()&&
                address.state.trim().isNotEmpty()&&
                address.phoneNumber.trim().isNotEmpty()&&
                address.userFullName.trim().isNotEmpty()
    }

    fun deleteAddress(address: ProductAddress){
        val validateInputs = validateInputs(address)
        if (validateInputs){
            firestore.collection("user").document(auth.uid!!).collection("address").document().delete()

        }
    }
}