package com.gamecodeschool.recipeapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//build api

object RetrofitInstance {

    val api:MealApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApi::class.java)
    }
}