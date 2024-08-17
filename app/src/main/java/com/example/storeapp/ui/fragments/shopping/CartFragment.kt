package com.example.storeapp.ui.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.utils.showNavView
import com.example.storeapp.viewmodel.DetailsViewModel


class CartFragment : Fragment(R.layout.fragment_cart) {
    lateinit var binding : FragmentCartBinding
    private val cartViewModel by viewModels<DetailsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNavView()
    }
}