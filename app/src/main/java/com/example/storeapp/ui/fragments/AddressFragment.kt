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
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentAddressBinding
import com.example.storeapp.models.ProductAddress
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressFragment : Fragment(R.layout.fragment_address) {
lateinit var binding: FragmentAddressBinding
val addressViewModel by viewModels<AddressViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddressBinding.bind(view)
        binding.apply {

            buttonSave.setOnClickListener {
                val productAddress = getDataFromET()
                addressViewModel.addAddress(productAddress)


            }
        }
        lifecycleScope.launch {
            addressViewModel.inputError.collect{
                  if(it.isNotEmpty()){
                      Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                  }
            }
        }
        lifecycleScope.launch {
            addressViewModel.addNewAddress.collectLatest {
                when(it){
                    is Resources.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext()," Make Sure You Input All Fields ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }


    }

    private fun getDataFromET() : ProductAddress{
        binding.apply {
            val addressTitle = edAddressTitle.text.toString()
            val fullName = edFullName.text.toString()
            val phone = edPhone.text.toString()
            val city = edCity.text.toString()
            val state = edState.text.toString()
            val street = edStreet.text.toString()

            val productAddress = ProductAddress(addressTitle,fullName,street,phone,city,state)
            return productAddress
        }
    }

}