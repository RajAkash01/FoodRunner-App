package com.example.myapplication.database


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class],version = 3)
abstract class FoodDatabase:RoomDatabase() {
    abstract fun foodDao():FoodDao

}