package com.gamecodeschool.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gamecodeschool.recipeapp.activities.MainActivity
import com.gamecodeschool.recipeapp.activities.MealActivity
import com.gamecodeschool.recipeapp.adapters.FavoritesMealsAdapter
import com.gamecodeschool.recipeapp.databinding.FragmentFavoritesBinding
import com.gamecodeschool.recipeapp.videoModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter:FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()
        onMealClick()

        val itemTouchHelper = object :  ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList.getOrNull(position)

                if (deletedMeal != null) {
                    viewModel.deleteMeal(deletedMeal)

                    Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                        "Undo"
                    ) {
                        viewModel.insertMeal(deletedMeal)
                    }.show()
                } else {
                    Toast.makeText(requireContext(), "Meal not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }



    private fun prepareRecyclerView() {
        favoritesAdapter = FavoritesMealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    //observe((viewLifeCycleOwner)
    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer{ meals ->
            favoritesAdapter.differ.submitList(meals)
        })
    }

    private fun onMealClick() {
        favoritesAdapter.onItemClick = { meal->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }

}