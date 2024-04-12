package com.gamecodeschool.recipeapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gamecodeschool.recipeapp.pojo.Meal

//version increments after database changes
@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao():MealDao

    companion object{
        // Any change on this instance will be visible by any other thread
        @Volatile
        var INSTANCE:MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context):MealDatabase{
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as MealDatabase
        }
    }
}