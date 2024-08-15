package com.example.storeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel


class BestDealsViewModel(private val firestore:FirebaseFirestore):ViewModel() {

}