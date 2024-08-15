package com.example.storeapp.ui.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.SpecialProductsAdapter
import com.example.storeapp.databinding.FragmentChairBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.showNavView
import com.example.storeapp.viewmodel.BaseCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChairFragment : Fragment(R.layout.fragment_chair) {
lateinit var binding : FragmentChairBinding
private val baseCategoryViewModel by viewModels<BaseCategoryViewModel>()
    private val specialProductsAdapter = SpecialProductsAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChairBinding.bind(view)
        showNavView()

        setupRecyclerView()
        baseCategoryViewModel.getChairs()

        setupItemClickListener()

        lifecycleScope.launch {
            baseCategoryViewModel.chairs.collect{
                when(it){
                    is Resources.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext(),"Sorry Couldn't Fetch Data ", Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        showLoading()
                    }
                    is Resources.Success -> {
                        hideLoading()
                        if(it.data!!.isNotEmpty()){
                            specialProductsAdapter.differ.submitList(it.data)
                            Log.d("ChairFragment",it.data.size.toString())
                        }
                    }
                }
            }
        }




    }
    fun setupItemClickListener(){
        specialProductsAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productInfoFragment,bundle)

        }
    }

    fun setupRecyclerView(){
        binding.chairsRV.adapter = specialProductsAdapter
        binding.chairsRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun hideLoading() {
        binding.chairPR.visibility =View.INVISIBLE
    }
    private fun showLoading() {
        binding.chairPR.visibility =View.VISIBLE
    }
}