package com.gamecodeschool.recipeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.gamecodeschool.recipeapp.adapters.CategoryMealsAdapter
import com.gamecodeschool.recipeapp.databinding.ActivityCategoryMealsBinding
import com.gamecodeschool.recipeapp.fragments.HomeFragment
import com.gamecodeschool.recipeapp.pojo.Meal
import com.gamecodeschool.recipeapp.videoModel.CategoryMealsViewModel
import androidx.fragment.app.Fragment

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding : ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var randomMeal: Meal

    companion object{
        const val MEAL_ID = "com.gamecodeschool.recipeapp.fragments.idMeal"
        const val MEAL_NAME = "com.gamecodeschool.recipeapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.gamecodeschool.recipeapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.gamecodeschool.recipeapp.fragments.categoryName"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            categoryMealsAdapter.setMealsList(mealsList)

        })

        onMealClick()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

    private fun onMealClick() {
        categoryMealsAdapter.onItemClick = { meal->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }
}