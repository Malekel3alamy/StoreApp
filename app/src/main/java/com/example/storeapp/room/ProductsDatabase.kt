package com.example.storeapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.storeapp.models.CartProduct

/*

@Database(
    entities = [CartProduct::class] ,
    version = 1
)
@TypeConverters(TypeConverter::class)
abstract class ProductsDatabase : RoomDatabase(){

    abstract fun getDao() :DatabaseDao

    companion object{
        @Volatile
        private var productsDatabaseInstance :ProductsDatabase? = null

        operator fun invoke (context: Context) : ProductsDatabase{
            return Room.databaseBuilder(context.applicationContext,ProductsDatabase::class.java,
                "productsdb.db").build()
        }
    }
}*/