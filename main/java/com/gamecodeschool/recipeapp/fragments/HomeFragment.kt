package com.gamecodeschool.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gamecodeschool.recipeapp.databinding.FragmentHomeBinding
import com.gamecodeschool.recipeapp.pojo.Meal
import com.gamecodeschool.recipeapp.videoModel.HomeViewModel

//import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamecodeschool.recipeapp.R
import com.gamecodeschool.recipeapp.activities.CategoryMealsActivity
import com.gamecodeschool.recipeapp.activities.MainActivity
import com.gamecodeschool.recipeapp.activities.MealActivity
import com.gamecodeschool.recipeapp.adapters.CategoriesAdapter
import com.gamecodeschool.recipeapp.adapters.MostPopularAdapter
import com.gamecodeschool.recipeapp.pojo.MealsByCategory

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.gamecodeschool.recipeapp.fragments.idMeal"
        const val MEAL_NAME = "com.gamecodeschool.recipeapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.gamecodeschool.recipeapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.gamecodeschool.recipeapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onSearchIconClick()


    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply{
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories ->
                categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)

        }
    }


    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner,
            {meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal;
            })
    }

//    private fun observeRandomMeal() {
//        homeMvvm.observeRandomMealLivedata().observe(viewLifecycleOwner,
//            object : Observer<Meal>{
//                override fun onChanged(t: Meal) {
//                    Glide.with(this@HomeFragment)
//                        .load(t!!.strMealThumb)
//                        .into(binding.imgRandomMeal)
//                }
//            })
//    }

}