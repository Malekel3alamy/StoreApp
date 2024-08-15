package com.example.storeapp.ui.fragments.shopping

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.storeapp.R
import com.example.storeapp.adapters.HomeViewPagerAdapter
import com.example.storeapp.databinding.FragmentHomeBinding
import com.example.storeapp.ui.fragments.categories.AccessoryFragment
import com.example.storeapp.ui.fragments.categories.ChairFragment
import com.example.storeapp.ui.fragments.categories.CupboardFragment
import com.example.storeapp.ui.fragments.categories.FurnitureFragment
import com.example.storeapp.ui.fragments.categories.MainCategoryFragment
import com.example.storeapp.ui.fragments.categories.TableFragment
import com.example.storeapp.utils.showNavView
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment(R.layout.fragment_home) {
   lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =FragmentHomeBinding.bind(view)
        showNavView()


        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            FurnitureFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment()
        )

        val homeViewPagerAdapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter = homeViewPagerAdapter
        TabLayoutMediator(binding.tableLayout,binding.viewPagerHome){tab,position ->
              when(position){
                  0 -> tab.text = "Home"
                  1 -> tab.text = "Chair"
                  2 -> tab.text = "Furniture"
                  3 -> tab.text = "Cupboard"
                  4 -> tab.text = "Table"
                  5 -> tab.text = "Accessories"
              }
        }.attach()
    }
}