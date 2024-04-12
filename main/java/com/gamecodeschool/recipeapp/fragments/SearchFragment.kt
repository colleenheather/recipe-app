package com.gamecodeschool.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.transition.Transition.ViewAdapter
import com.gamecodeschool.recipeapp.R
import com.gamecodeschool.recipeapp.activities.MainActivity
import com.gamecodeschool.recipeapp.activities.MealActivity
import com.gamecodeschool.recipeapp.adapters.FavoritesMealsAdapter
import com.gamecodeschool.recipeapp.databinding.FragmentSearchBinding
import com.gamecodeschool.recipeapp.videoModel.HomeViewModel

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var searchRecyclerViewAdapter: FavoritesMealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        binding.imgSearchArrow.setOnClickListener{ searchMeals() }

        observeSearchedMealsLiveData()
        onMealClick()
    }

    private fun onMealClick() {
        searchRecyclerViewAdapter.onItemClick = { meal->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner, Observer {mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)

        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = FavoritesMealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }

}