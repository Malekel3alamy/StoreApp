package com.example.storeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.storeapp.R
import com.example.storeapp.adapters.ProductColorsAdapter
import com.example.storeapp.databinding.FragmentProductInfoBinding
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.Product
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.hideNavView
import com.example.storeapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment : Fragment(R.layout.fragment_product_info) {
    lateinit var binding : FragmentProductInfoBinding
    private val productColorsAdapter = ProductColorsAdapter()
    private val cartViewModel by viewModels<DetailsViewModel>()
    var product =Product()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          binding = FragmentProductInfoBinding.bind(view)
             hideNavView()
             hideLoading()


        binding.backImage.setOnClickListener {
            findNavController().navigateUp()
        }

        if (arguments!= null){

             product = requireArguments().getParcelable<Product>("Product")!!

            binding.apply {
                productName.text = product.name
                Glide.with(requireContext()).load(product.images[0]).into(productImage)
                productPriceTV.text = "$ ${product.price}"
                if ((product.offerPercentage) != null ){
                    val discount = product.price * product.offerPercentage!!
                    if (discount == 0f ){
                        productPriceAfterOfferTV.visibility = View.GONE
                        productPriceTV.setTextColor(resources.getColor(R.color.black))
                    }else
                    productPriceAfterOfferTV.text = (product.price - discount).toString() + " $"


                }else{
                    productPriceAfterOfferTV.visibility = View.GONE
                    productPriceTV.setTextColor(resources.getColor(R.color.black))
                }
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
        binding.btnAddToCart.setOnClickListener {
            cartViewModel.addUpdateProductInCart(CartProduct(product,1,product.colors))
          //  findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
            lifecycleScope.launch {
                cartViewModel.addToCart.collect{
                    when(it){
                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(requireContext()," Sorry Error ",Toast.LENGTH_LONG).show()
                        }
                        is Resources.Loading -> {
                            showLoading()
                        }
                        is Resources.Success -> {
                            hideLoading()
                           Toast.makeText(requireContext()," This Product Is Now in Your Cart ",Toast.LENGTH_SHORT).show()
                        }
                        is Resources.UnSpecified -> Unit
                    }
                }
            }
        }



    }
    fun setupColorRV(){

        binding.colorsRV.adapter = productColorsAdapter
        binding.colorsRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

    private fun hideLoading() {
        binding.detailsPR.visibility =View.INVISIBLE

    }
    private fun showLoading() {
        binding.detailsPR.visibility =View.VISIBLE
    }

}