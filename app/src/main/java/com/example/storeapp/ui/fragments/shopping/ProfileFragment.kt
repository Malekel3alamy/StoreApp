package com.example.storeapp.ui.fragments.shopping

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
import com.example.storeapp.viewmodel.UserAccountViewModel
import com.google.firebase.database.collection.LLRBNode
import com.google.type.Color
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var binding : FragmentProfileBinding

    private val userAccountViewModel by viewModels<UserAccountViewModel>()

    private var imageUri : Uri? = null

    private lateinit var imageActivityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            imageUri =it.data?.data
            Glide.with(this).load(imageUri).into(binding.imageUser)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)

        userAccountViewModel.getUserInfo()

        lifecycleScope.launch {
            userAccountViewModel.user.collectLatest {
                when(it){
                    is Resources.Error -> {
                        hideUseRLoading()
                        Toast.makeText(requireContext()," Sorry Error ", Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                            showUserLoading()
                    }
                    is Resources.Success -> {
                        hideUseRLoading()
                        showUseInfo(it.data!!)

                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }

        lifecycleScope.launch {
            userAccountViewModel.editInfo.collectLatest {
                when(it){
                    is Resources.Error -> {
                        hideLoading()
                        Toast.makeText(requireContext()," Sorry Error ", Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {
                        showLoading()
                    }
                    is Resources.Success -> {
                        hideLoading()
                        findNavController().navigateUp()

                    }
                    is Resources.UnSpecified -> Unit
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            binding.apply {
                val email = edEmail.text.toString().trim()
                val firstName = edFirstName.text.toString().trim()
                val lastName = edLastName.text.toString().trim()
                val user = User(firstName,lastName,email)

                userAccountViewModel.updateUser(user,imageUri)
            }
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type  = "image/*"

            imageActivityResultLauncher.launch(intent)
        }
    }

    private fun showLoading() {
        binding.progressbarAccount.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressbarAccount.visibility = View.GONE
    }

    private fun showUseInfo(user: User) {
          Glide.with(requireContext()).load(user.imagePath).error(ColorDrawable(resources.getColor(R.color.g_gray700))).into(binding.imageUser)
          binding.edEmail.setText(user.email)
        Log.d("Email",user.email)
        binding.edFirstName.setText(user.firstName)
        binding.edLastName.setText(user.lastName)

    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    private fun hideUseRLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }
}