package com.gamecodeschool.recipeapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.gamecodeschool.recipeapp.R;
import com.gamecodeschool.recipeapp.databinding.ActivityMealBinding;
import com.gamecodeschool.recipeapp.db.MealDatabase
import com.gamecodeschool.recipeapp.fragments.HomeFragment
import com.gamecodeschool.recipeapp.pojo.Meal
import com.gamecodeschool.recipeapp.videoModel.HomeViewModel
import com.gamecodeschool.recipeapp.videoModel.MealViewModel
import com.gamecodeschool.recipeapp.videoModel.MealViewModelFactory

class MealActivity: AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding:ActivityMealBinding
    private lateinit var mealMvvm:MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationInViews()

        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFavorites.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this, object: Observer<Meal> {
            override fun onChanged(t: Meal) {
                val meal = t
                mealToSave = meal

                binding.tvCategory.text = "Category : ${meal.strCategory}"
                binding.tvArea.text = "Area : ${meal.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgRandomMeal)

        binding.mealName.text = mealName
        //binding.collapsingToolBar.title = mealName

    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
}