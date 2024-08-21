package com.example.storeapp.ui.fragments.shopping

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.CartAdapter
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.firebase.FirebaseCommon
import com.example.storeapp.models.CartProduct
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.showNavView
import com.example.storeapp.viewmodel.CartViewModel
import com.example.storeapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    lateinit var binding : FragmentCartBinding
    private val cartViewModel by viewModels<CartViewModel>()
    private val cartAdapter = CartAdapter()

    private var total = 0f



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        showNavView()
        setupRecycler()

        binding.btnCheckOut.setOnClickListener {
            if (cartAdapter.differ.currentList.size != 0  ){

            }
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(cartAdapter.differ.currentList.toTypedArray(),total)

            findNavController().navigate(action)
        }

        cartAdapter.onProductClickListener = {
            val bundle = Bundle().apply {
                putParcelable("Product",it.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productInfoFragment,bundle)
        }

        // Increase Product Quantity
        cartAdapter.onPlusClickListener ={
               cartViewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)
        }

        handleDeleteProduct()

        // Decrease Product Quantity
        cartAdapter.onMinusClickListener={
                cartViewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }

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
                        if (cartAdapter.differ.currentList.size ==0){
                            showEmptyBox()
                        }

                        it.data?.let {cartProductList ->
                            total =  setTotalPriceTV(cartProductList)
                            binding.totalPriceTV.text = total.toString() + " $ "


                        }

                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }
    }

    private fun handleDeleteProduct() {
        lifecycleScope.launch {
            cartViewModel.deleteDialog.collectLatest{
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle(" Delete Product From Cart")
                    setMessage("Delete This Product From Cart ? ")
                    setPositiveButton("Delete"){dialog,_ ->
                        cartViewModel.deleteCarTProduct(it)
                        dialog.dismiss()

                    }
                    setNegativeButton("Cancel"){dialog,_ ->
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }
    }

    private fun setupRecycler(){
        binding.apply {
            rvCart.adapter = cartAdapter
            rvCart.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
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

    private  fun setTotalPriceTV(cartProductsList:List<CartProduct>) : Float{
        var totalPrice =  0f
        for (i in 0..<cartProductsList.size){
            if (cartProductsList[i].product.offerPercentage != null){
                val discount = cartProductsList[i].product.price * cartProductsList[i].product.offerPercentage!!
                val newPrice = (cartProductsList[i].product.price) - discount
                totalPrice += newPrice * cartProductsList[i].productQuantity
            }else{
                totalPrice += (cartProductsList[i].product.price)*cartProductsList[i].productQuantity
            }
        }
        return totalPrice
    }
}