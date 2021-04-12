package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FoodDao {

    @Insert
    fun insertfood(foodEntity: FoodEntity)

    @Delete
    fun deletefood(foodEntity: FoodEntity)

    @Query("SELECT *  FROM foods")
    fun getAllfoods():List<FoodEntity>

    @Query("SELECT * FROM foods WHERE food_id=:foodId")
    fun getfoodById(foodId:String):FoodEntity
}