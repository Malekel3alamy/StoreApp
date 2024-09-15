package com.example.storeapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.AddressAdapter
import com.example.storeapp.adapters.BillingProductsAdapter
import com.example.storeapp.databinding.FragmentBillingBinding
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.ProductAddress
import com.example.storeapp.models.order.Order
import com.example.storeapp.models.order.OrderStatus
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.BillingViewModel
import com.example.storeapp.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment(R.layout.fragment_billing) {
    lateinit var binding: FragmentBillingBinding

    private var productAddress :ProductAddress?= null

    private val billingProductsAdapter by lazy { BillingProductsAdapter()}
   // private val addressAdapter by lazy { AddressAdapter() }

    private val billingViewModel by viewModels<BillingViewModel>()

    private val ordersViewModel by viewModels<OrderViewModel>()



    private val args by navArgs<BillingFragmentArgs>()
    private var cartProducts = emptyList<CartProduct>()
    private var totalPrice = 0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    private fun getCartArguments() {

        cartProducts = args.cartProducts!!.toList()
        totalPrice = args.totalPrice
        productAddress=args.Address
        Log.d("Price",totalPrice.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBillingBinding.bind(view)

        setupBillingProductsRv()
       // setupAddressRv()
        getCartArguments()


            if (productAddress!= null){
                binding.buttonAddress.visibility = View.VISIBLE
                binding.buttonAddress.setText(productAddress!!.addressTitle)

            }

        binding.buttonAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }



        lifecycleScope.launch {
            ordersViewModel.order.collectLatest {
                when(it){
                    is Resources.Error -> {
                        binding.placeOrderPR.visibility = View.GONE
                        Toast.makeText(requireContext(),"Sorry Error ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        binding.placeOrderPR.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.placeOrderPR.visibility = View.GONE
                        Toast.makeText(requireContext()," You Products Have Been Ordered Successfully ",Toast.LENGTH_LONG).show()

                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(cartProducts)
        binding.tvTotalPrice.text =   totalPrice.toString()


        binding.buttonPlaceOrder.setOnClickListener {

            if (productAddress == null  && totalPrice!= 0f && cartProducts != emptyList<CartProduct>() ){
                Toast.makeText(requireContext()," Please Make Sure To Select Address and Products ",Toast.LENGTH_LONG).show()

            }else{
                showOrderConfirmationDialog()

            }
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(" Order Products    ")
            setMessage("Do You Want To Order These Products ? ")
            setPositiveButton("Yes"){dialog,_ ->

                val order = Order(OrderStatus.Ordered.orderStatus,totalPrice,cartProducts, productAddress!!)
                ordersViewModel.placeOrder(order)


              //  val action = BillingFragmentDirections.actionBillingFragmentToCartFragment(true)
             findNavController().navigate(R.id.action_billingFragment_to_cartFragment)
                dialog.dismiss()

            }
            setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }



    private fun setupBillingProductsRv() {
        binding.rvProducts.adapter = billingProductsAdapter
        binding.rvProducts.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }
}