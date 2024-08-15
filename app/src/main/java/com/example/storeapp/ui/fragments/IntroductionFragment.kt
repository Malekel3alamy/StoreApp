package com.example.storeapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storeapp.R
import com.example.storeapp.databinding.ActivityLoginRegisterBinding
import com.example.storeapp.databinding.FragmentIntroductionBinding
import com.example.storeapp.ui.ShoppingActivity
import com.example.storeapp.utils.Contants.Companion.INTRODUCTION_FRAGMENT_STATE_KEY
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    lateinit var  binding: FragmentIntroductionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentIntroductionBinding.bind(view)
        val sharedPreferences =requireActivity()
            .getSharedPreferences("IntroductionActivity",Context.MODE_PRIVATE)
      /*    lifecycleScope.launch {
               if (sharedPreferences.getBoolean(INTRODUCTION_FRAGMENT_STATE_KEY,false) ){

                   findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
               }
           }*/
        runBlocking {
            if (sharedPreferences.getBoolean(INTRODUCTION_FRAGMENT_STATE_KEY,false) ){

                findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)
            }
        }
        binding.startButtonIntroductionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)

            sharedPreferences .apply {
                    edit().putBoolean(INTRODUCTION_FRAGMENT_STATE_KEY,true).apply()
                }
        }

        super.onViewCreated(view, savedInstanceState)
    }

}