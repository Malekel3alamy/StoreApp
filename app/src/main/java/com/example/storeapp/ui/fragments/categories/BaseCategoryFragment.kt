package com.example.storeapp.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.SpecialProductsAdapter
import com.example.storeapp.databinding.FragmentBaseCategoryBinding
import com.example.storeapp.viewmodel.BaseCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    lateinit var binding : FragmentBaseCategoryBinding
    val baseCategoryViewModel by viewModels<BaseCategoryViewModel>()
     val specialProductsAdapter = SpecialProductsAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBaseCategoryBinding.bind(view)
        setupRecyclerView()

    }

    fun setupRecyclerView(){
        binding.baseCategoryRV.adapter = specialProductsAdapter
        binding.baseCategoryRV.layoutManager = LinearLayoutManager(requireContext())
    }

    fun hideLoading() {
        binding.BasePR.visibility =View.INVISIBLE
    }
    fun showLoading() {
        binding.BasePR.visibility =View.VISIBLE
    }

    fun setupItemClickListener(){
        specialProductsAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productInfoFragment,bundle)

        }
    }

}