package com.example.storeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentAddressBinding
import com.example.storeapp.models.CartProduct
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

    private lateinit var productAddress : ProductAddress

    private val args by navArgs<AddressFragmentArgs>()
    private var cartProducts = emptyList<CartProduct>()
    private var totalPrice = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartProducts = args.cartProducts.toList()
        totalPrice = args.totalPrice

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddressBinding.bind(view)
        binding.apply {

            buttonSave.setOnClickListener {

                if (checkInputs()){
                    addressViewModel.addAddress(productAddress)
                    val action = AddressFragmentDirections.actionAddressFragmentToBillingFragment(productAddress,
                        cartProducts = cartProducts.toTypedArray(), totalPrice = totalPrice)
                    findNavController().navigate(action)
                }
            }

            imageAddressClose.setOnClickListener {
                findNavController().navigate(R.id.action_addressFragment_to_cartFragment)
            }

        }




    }

    private fun checkAddingAddressToDatabaseStatus(){
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


                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }
    }



    fun checkInputs():Boolean{
        binding.apply {
            val addressTitle = edAddressTitle.text.toString()
            val fullName = edFullName.text.toString()
            val phone = edPhone.text.toString()
            val city = edCity.text.toString()
            val state = edState.text.toString()
            val street = edStreet.text.toString()
            if (addressTitle.isEmpty() || fullName.isEmpty()||phone.isEmpty()||city.isEmpty()||state.isEmpty()||street.isEmpty()){
                Toast.makeText(requireContext()," Make Sure You Input All Fields ",Toast.LENGTH_LONG).show()
                 return false
            }else{
                productAddress =  ProductAddress(addressTitle,fullName,street,phone,city,state)
                return true
            }
        }

    }

}