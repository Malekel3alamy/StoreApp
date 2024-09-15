package com.example.storeapp.ui.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.R
import com.example.storeapp.adapters.BestDealsAdapter
import com.example.storeapp.adapters.BestProductsAdapter
import com.example.storeapp.adapters.SpecialProductsAdapter
import com.example.storeapp.databinding.FragmentMainCategoryBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
lateinit var binding: FragmentMainCategoryBinding

private val  specialProductsAdapter = SpecialProductsAdapter()
     val mainCategoryViewModel by viewModels<MainCategoryViewModel>()

    private val bestDealsAdapter = BestDealsAdapter()

    private val bestProductsAdapter = BestProductsAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){

        fragmentManager?.clearBackStack("")
        }
        callback.isEnabled

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainCategoryBinding.bind(view)



        // Setup Recyclers View
        setupSpecialProductRV()
        setupBestDealsRV()
        setupBestProductsRV()



        lifecycleScope.launch {
            mainCategoryViewModel.specialProducts.collect{
                       when(it){
                           is Resources.Error -> {
                               hideLoading()
                               Toast.makeText(requireContext(),"Sorry Couldn't Fetch Data ",Toast.LENGTH_LONG).show()
                           }
                           is Resources.Loading -> {
                               showLoading()
                           }
                           is Resources.Success -> {
                               if(it.data!!.isNotEmpty()){
                                   specialProductsAdapter.differ.submitList(it.data)
                               }

                           }
                           is Resources.UnSpecified -> Unit
                       }
            }
        }

        specialProductsAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productInfoFragment,bundle)

        }


        // Best Deals
        lifecycleScope.launch {
            mainCategoryViewModel.bestDeals.collect{
                when(it){
                    is Resources.Error -> {
                        Toast.makeText(requireContext(),"Sorry Couldn't Fetch Data ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(it.data!!.isNotEmpty()){
                            bestDealsAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }
        bestDealsAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productInfoFragment,bundle)

        }

        // Best Products
        lifecycleScope.launch {
            mainCategoryViewModel.bestProducts.collect{
                when(it){
                    is Resources.Error -> {
                        Toast.makeText(requireContext(),"Sorry Couldn't Fetch Data ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> { }
                    is Resources.Success -> {
                      hideLoading()
                        if(it.data!!.isNotEmpty()){
                            bestProductsAdapter.differ.submitList(it.data)
                            Log.d("MainCategoryFragment",it.data.size.toString())
                        }

                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }

        bestProductsAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productInfoFragment,bundle)

        }

        // handle Horizontal RecyclerView  Scroll
        handleHorizontalScroll()

    }

    private fun hideLoading() {
        binding.mainCategoryLayout.visibility= View.VISIBLE
        binding.mainCategoryShimmerLayout.stopShimmer()
        binding.mainCategoryShimmerLayout.isVisible = false
    }
    private fun showLoading() {

        binding.mainCategoryLayout.visibility= View.INVISIBLE
        binding.mainCategoryShimmerLayout.startShimmer()
        binding.mainCategoryShimmerLayout.isVisible = true
    }

    private fun setupSpecialProductRV() {
        binding.rvSpecialProducts.apply {
            adapter =specialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }

    }

    private fun setupBestDealsRV() {

        binding.rvBestDeals.apply {
            adapter =bestDealsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupBestProductsRV() {
        binding.rvBestProducts.apply {
            adapter =bestProductsAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun handleHorizontalScroll(){
        val listener = object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                if (rv.canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent
                            .requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }
                else {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent
                            .requestDisallowInterceptTouchEvent(false)
                    }
                    rv.removeOnItemTouchListener(this)
                    return true
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

      binding.rvSpecialProducts.addOnItemTouchListener(listener)
      binding.rvBestDeals.addOnItemTouchListener(listener)
    }


}