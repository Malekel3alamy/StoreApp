package com.example.storeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.storeapp.R
import com.example.storeapp.adapters.ProductColorsAdapter
import com.example.storeapp.databinding.FragmentProductInfoBinding
import com.example.storeapp.models.Product
import com.example.storeapp.utils.hideNavView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductInfoFragment : Fragment(R.layout.fragment_product_info) {
    lateinit var binding : FragmentProductInfoBinding
    private val productColorsAdapter = ProductColorsAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          binding = FragmentProductInfoBinding.bind(view)

             hideNavView()

        if (arguments!= null){

            val product = requireArguments().getParcelable<Product>("Product")

            binding.apply {
                productName.text = product?.name
                Glide.with(requireContext()).load(product!!.images[0]).into(productImage)
                productPrice.text = "$ ${product.price}"
                productDescription.text = product.description
                setupColorRV()
                if (product.colors != null){
                    productColorsAdapter.differ.submitList(product.colors)
                }else{
                    binding.productColorsTV.visibility = View.GONE
                    productColorsAdapter.differ.submitList(emptyList())
                }

            }
        }

    }
    fun setupColorRV(){

        binding.colorsRV.adapter = productColorsAdapter
        binding.colorsRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

}