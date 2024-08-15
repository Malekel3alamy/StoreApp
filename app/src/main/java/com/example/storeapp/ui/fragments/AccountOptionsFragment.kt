package com.example.storeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentAccountOptionsBinding
import com.example.storeapp.databinding.FragmentIntroductionBinding


class AccountOptionsFragment : Fragment(R.layout.fragment_account_options) {
    private  lateinit var binding: FragmentAccountOptionsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAccountOptionsBinding.bind(view)
/*
        binding.apply {
            buttonLoginAccountOptions.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
            }
            buttonRegisterAccountOptions.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
            }
        }*/
        super.onViewCreated(view, savedInstanceState)
    }


}