package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.models.ProductAddress
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class BillingViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val auth : FirebaseAuth
) : ViewModel() {

    private val _addresses = MutableStateFlow<Resources<List<ProductAddress>>>(Resources.Loading())
    val addresses  = _addresses.asStateFlow()

    init {
        getUserAddresses()
    }
    private fun getUserAddresses(){
        firestore.collection("user").document(auth.uid!!).collection("address")
            .addSnapshotListener { value, error ->
                if (error != null){
                    viewModelScope.launch {
                        _addresses.emit(Resources.Error(error.message.toString()))
                    }
                }else{
                    viewModelScope.launch {
                        val addresses = value?.toObjects(ProductAddress::class.java)
                        _addresses.emit(Resources.Success(addresses!!))
                    }
                }
            }
    }
}