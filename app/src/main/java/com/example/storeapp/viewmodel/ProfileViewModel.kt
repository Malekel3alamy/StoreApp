package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.User
import com.example.storeapp.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth):ViewModel() {


        private val _user = MutableStateFlow<Resources<User>>(Resources.UnSpecified())
    val user = _user.asStateFlow()

    init{
        getUser()
    }

    fun getUser(){
        viewModelScope.launch {
            _user.emit(Resources.Loading())
        }
        firestore.collection("user").document(auth.uid!!).addSnapshotListener { value, error ->
            if (error == null){
                val user = value!!.toObject(User::class.java)
                viewModelScope.launch {
                    _user.emit(Resources.Success(user!!))
                }
            }else{
                viewModelScope.launch {
                    _user.emit(Resources.Error(error.message.toString()))
                }
            }
        }
    }

    fun logout(){
        auth.signOut()
    }
}