package com.example.storeapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.BillingProductsAdapter
import com.example.storeapp.adapters.OrderDetailsAdapter
import com.example.storeapp.databinding.FragmentOrderDetailBinding
import com.example.storeapp.models.order.Order
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailsFragment : Fragment(R.layout.fragment_order_detail) {
    lateinit var binding : FragmentOrderDetailBinding


    private val orderDetailsAdapter = OrderDetailsAdapter()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailBinding.bind(view)

        setupOrderDetailsRV()
        if (arguments != null){
            val order  = requireArguments().getParcelable<Order>("Order")
            if (order != null){
                binding.tvOrderId.text = order.orderId.toString()
                binding.tvTotalPrice.text=order.totalPrice.toString()
                binding.tvAddress.text = order.address.addressTitle
                binding.tvPhoneNumber.text = order.address.phoneNumber
                binding.tvFullName.text = order.address.userFullName
                binding.tvTotalPrice.text = order.totalPrice.toString()

                orderDetailsAdapter.differ.submitList(order.products)

            }

        }else{
            Toast.makeText(requireContext()," Sorry Error ",Toast.LENGTH_LONG).show()
        }



    }

    private fun setupOrderDetailsRV() {
        binding.rvProducts.apply {
            adapter = orderDetailsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}