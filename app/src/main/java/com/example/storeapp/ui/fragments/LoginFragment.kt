package com.example.storeapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentLoginBinding
import com.example.storeapp.ui.ShoppingActivity
import com.example.storeapp.ui.dialog.setupResetPasswordDialog
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
private lateinit var binding : FragmentLoginBinding
val firebase = Firebase.auth
private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        if (firebase.currentUser!= null){
            findNavController().navigate(R.id.action_loginFragment_to_shoppingActivity)
        }
if(arguments != null){
    val email = requireArguments().getString("email")
    binding.etEmailLogin.setText(email)
        }

        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)
        }

        binding.buttonLoginAccountOptions.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val passwoed = binding.etPasswordLogin.text.toString()

            loginViewModel.login(email,passwoed)
            lifecycleScope.launch {
                loginViewModel.login.collect{
                    when(it){
                        is Resources.Error -> {

                            Toast.makeText(requireContext()," FailedTo Log In ",Toast.LENGTH_SHORT).show()
                        }
                        is Resources.Loading -> {

                        }
                        is Resources.Success -> {
                            Intent(requireActivity(),ShoppingActivity::class.java).also {
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(it)
                            }
                        }
                        is Resources.UnSpecified -> Unit
                    }
                }
            }
        }

binding.tvForgetPasswordLogin.setOnClickListener {
       setupResetPasswordDialog { email ->

           loginViewModel.resetPassword(email)
           lifecycleScope.launch {
               loginViewModel.resetPassword.collect{
                   when(it){
                       is Resources.Error -> {
                           Toast.makeText(requireContext()," Failed To Send Email ",Toast.LENGTH_LONG).show()
                       }
                       is Resources.Loading -> {

                       }
                       is Resources.Success -> {

                       }
                       is Resources.UnSpecified -> Unit
                   }
               }
           }

       }
}


    }
}
