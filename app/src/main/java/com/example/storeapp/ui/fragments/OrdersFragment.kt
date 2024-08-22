package com.example.storeapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.AllOrdersAdapter
import com.example.storeapp.databinding.FragmentOrdersBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment:Fragment(R.layout.fragment_orders) {
    lateinit var binding : FragmentOrdersBinding

    private val allOrdersViewModel by viewModels<AllOrdersViewModel>()
    private val allOrdersAdapter = AllOrdersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrdersBinding.bind(view)

        setupAllOrdersRecycler()

        allOrdersAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable("Order",it)
            }
            findNavController().navigate(R.id.action_ordersFragment_to_orderDetailsFragment,bundle)
        }
        lifecycleScope.launch {
            allOrdersViewModel.allOrders.collectLatest {
                when(it){
                    is Resources.Error -> {
                     binding.progressbarAllOrders.visibility = View.GONE
                        Toast.makeText(requireContext()," Sorry Error ${it.message.toString()}",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        allOrdersAdapter.differ.submitList(it.data)
                    }
                    is Resources.UnSpecified -> {
                        return@collectLatest Unit
                    }
                }
            }
        }

    }

    private fun setupAllOrdersRecycler() {

        binding.rvAllOrders.apply {
            adapter = allOrdersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

}