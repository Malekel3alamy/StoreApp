package com.example.storeapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.Product


@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: CartProduct):Boolean

    @Query("DELETE FROM productsTable")
    suspend fun deleteAllProducts() :Boolean


    @Query("SELECT * FROM productsTable")
    fun getAllProducts():LiveData<List<CartProduct>>

    @Query("SELECT * FROM productsTable WHERE id =id")
    suspend fun getProduct( id: String) :CartProduct

    @Query("DELETE FROM productsTable WHERE id =id")
    suspend fun deleteProduct(id:String) : Boolean
}