package com.example.storeapp.room

import androidx.room.TypeConverter

class TypeConverter {

    @TypeConverter
    fun fromList(list:List<String>) : String{
        return list.toString()
    }

    @TypeConverter
    fun toList(string:String):List<String>{
        return string.split(",")
    }
}