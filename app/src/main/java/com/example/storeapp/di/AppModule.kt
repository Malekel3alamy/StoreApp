package com.example.storeapp.di

import com.example.storeapp.firebase.FirebaseCommon
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseCommon(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth) = FirebaseCommon(firestore,firebaseAuth)

    @Provides
    @Singleton
    fun providesFirebaseStorage() = FirebaseStorage.getInstance().reference
}