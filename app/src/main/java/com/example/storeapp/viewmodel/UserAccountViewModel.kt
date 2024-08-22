package com.example.storeapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.User
import com.example.storeapp.di.KleineApp
import com.example.storeapp.utils.RegisterValidation
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val storage : StorageReference,
    app : Application
):AndroidViewModel(app)
{

    private val _user = MutableStateFlow<Resources<User>>(Resources.UnSpecified())
    val user = _user.asStateFlow()

    private val _editInfo = MutableStateFlow<Resources<User>>(Resources.UnSpecified())
    val editInfo = _editInfo.asStateFlow()

    fun getUserInfo(){
        viewModelScope.launch {
            _user.emit(Resources.Loading())
        }
        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                it?.let {
                    val user = it.toObject<User>()
                    if (user != null){
                        viewModelScope.launch {
                            _user.emit(Resources.Success(user))
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    fun updateUser(user: User,imageUrl : Uri?){
         val areInputsValid =  validateEmail(user.email) is RegisterValidation.Success
                 && user.firstName.trim().isNotEmpty()
                 && user.lastName.trim().isNotEmpty()
        if (!areInputsValid){
            viewModelScope.launch {
                _user.emit(Resources.Error("Check Your Inputs "))
            }

            return
        }
        viewModelScope.launch {
            _user.emit(Resources.Loading())
        }

        if (imageUrl == null){
            saveUserInformation(user,true)
        }else{
            saveUseInformationWithNewImage(user,imageUrl)
        }
    }

    private fun saveUseInformationWithNewImage(user: User, imageUrl: Uri) {
               viewModelScope.launch {
                   try {
                  val imageBitmap = MediaStore.Images.Media.
                  getBitmap(getApplication<KleineApp>().contentResolver,imageUrl)

                       // compress that image
                  val byteArrayOutputStream = ByteArrayOutputStream()
                  imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
                       val imageByteArray = byteArrayOutputStream.toByteArray()
                        val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                       val result = imageDirectory.putBytes(imageByteArray).await()
                       val imageUrl = result.storage.downloadUrl.await().toString()
                       saveUserInformation(user.copy(imagePath = imageUrl),false)

                   }catch (e:Exception){
                       viewModelScope.launch {
                           _editInfo.emit(Resources.Error(e.message.toString()))
                       }
                   }
               }
    }

    private fun saveUserInformation(user: User, shouldRetreiveOldImage: Boolean) {
              firestore.runTransaction { transaction ->
                  val documentRef = firestore.collection("user").document(auth.uid!!)
                 if (shouldRetreiveOldImage){
                     val currentUser = transaction.get(documentRef).toObject(User ::class.java)
                     val newUser = user.copy(imagePath =currentUser?.imagePath ?:"" )
                     transaction.set(documentRef,newUser)
                 }else{
                     transaction.set(documentRef,user)
                 }

              }.addOnSuccessListener {
                  viewModelScope.launch {
                      _editInfo.emit(Resources.Success(user))
                  }
              }.addOnFailureListener {
                  viewModelScope.launch {
                      _editInfo.emit(Resources.Error(it.message.toString()))
                  }
              }
    }

}