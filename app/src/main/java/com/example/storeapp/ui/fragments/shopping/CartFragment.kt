package com.example.storeapp.ui.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.CartAdapter
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.showNavView
import com.example.storeapp.viewmodel.CartViewModel
import com.example.storeapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    lateinit var binding : FragmentCartBinding
    private val cartViewModel by viewModels<CartViewModel>()
    private val cartAdapter = CartAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        showNavView()
        setupRecycler()
        cartViewModel.getCartProducts()

        lifecycleScope.launch {
            cartViewModel.cartProducts.collect{
                when(it){
                    is Resources.Error -> {
                        showEmptyBox()
                        Toast.makeText(requireContext()," Sorry Error ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        hideEmptyBox()
                        showLoading()
                    }
                    is Resources.Success -> {
                        hideEmptyBox()
                        hideLoading()
                         cartAdapter.differ.submitList(it.data)
                        var totalPrice =  0f
                        it.data?.let {cartProductList ->
                            for (i in 0..<cartProductList.size){

                                totalPrice += (cartProductList[i].product.price)*cartProductList[i].productQuantity
                            }
                            binding.totalPriceTV.text = totalPrice.toString() + " $ "
                        }

                    }
                }
            }
        }
    }

    private fun setupRecycler(){
        binding.apply {
            rvCart.adapter = cartAdapter
            rvCart.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showLoading(){
        binding.cartPR.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        binding.cartPR.visibility = View.INVISIBLE
    }

    private fun showEmptyBox(){
        binding.layoutCartEmpty.visibility = View.VISIBLE
    }
    private fun hideEmptyBox(){
        binding.layoutCartEmpty.visibility = View.GONE
    }
}