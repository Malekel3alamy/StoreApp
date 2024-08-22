package com.example.storeapp.ui.fragments.shopping

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.storeapp.R
import com.example.storeapp.data.User
import com.example.storeapp.databinding.FragmentProfileBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.ProfileViewModel
import com.example.storeapp.viewmodel.UserAccountViewModel
import com.google.firebase.database.collection.BuildConfig
import com.google.firebase.database.collection.LLRBNode
import com.google.type.Color
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
lateinit var binding : FragmentProfileBinding

private val profileViewModel by viewModels<ProfileViewModel>()

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           binding = FragmentProfileBinding.bind(view)
        lifecycleScope.launch {
            profileViewModel.user.collectLatest {
                when(it){
                    is Resources.Error -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Toast.makeText(requireContext()," Sorry Error ",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).into(binding.imageUser)
                        binding.tvUserName.text =  " ${it.data!!.firstName} ${it.data!!.lastName}"
                        }


                    is Resources.UnSpecified -> Unit
                }
            }
        }

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment2(
                emptyArray(),0f
            )

            findNavController().navigate(action)
        }

        binding.logout.setOnClickListener {
            profileViewModel.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment2)
            requireActivity().finish()
        }

        binding.tvVersion.text = " Version  ${BuildConfig.VERSION_CODE}"
    }
}

