package com.gamecodeschool.recipeapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gamecodeschool.recipeapp.pojo.Meal

@Dao
interface MealDao {
    // update and insert function in one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal:Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>



}