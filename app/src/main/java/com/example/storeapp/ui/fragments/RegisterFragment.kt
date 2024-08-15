package com.example.storeapp.ui.fragments

import android.nfc.Tag
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storeapp.R
import com.example.storeapp.data.User
import com.example.storeapp.databinding.FragmentRegisterBinding
import com.example.storeapp.utils.RegisterValidation
import com.example.storeapp.utils.Resources
import com.example.storeapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val TAG = "RegisterFragment"

    lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterBinding.bind(view)
      //  validateEmail()
      //  validatePassword()
        binding.apply {
            buttonRegisterAccountOptions.setOnClickListener {
                val user = User(
                   etFirstNameRegister.text.toString().trim(),
                    etLastNameRegister.text.toString().trim(),
                    etEmailRegister.text.toString().trim()
                )
               val password = etPasswordRegister.text.toString()

                registerViewModel.createAnAccountWithEmailAnPassword(user,password)

                lifecycleScope.launch {
                    registerViewModel.register.collect{

                        when(it){

                            is Resources.Loading ->{
                                binding.apply {
                                    // prRegister.visibility=View.VISIBLE
                                   //  buttonRegisterAccountOptions.visibility=View.INVISIBLE
                                }
                            }
                            is Resources.Success ->{
                                binding.apply {
                                    prRegister.visibility=View.INVISIBLE
                                    buttonRegisterAccountOptions.visibility=View.VISIBLE
                                }

                                Log.d(TAG," data -> ${it.data.toString()}")
                                val bundle = Bundle().apply {

                                    putString("email",user.email)
                                }
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment,bundle)
                            }

                            is Resources.Error -> {
                                binding.apply {
                                    prRegister.visibility=View.INVISIBLE
                                    buttonRegisterAccountOptions.visibility=View.VISIBLE
                                }
                                Log.d(TAG," Error -> ${it.message.toString()}")
                            }
                        }
            }
        }
            }
        }

lifecycleScope.launch {
    registerViewModel.validation.collect{ validation ->
        if(validation.email is RegisterValidation.Failed){

            withContext(Dispatchers.Main){
                binding.etEmailRegister.apply {
                    requestFocus()
                    error = validation.email.message
                }
            }
        }

        if(validation.password is RegisterValidation.Failed){

            withContext(Dispatchers.Main){
                binding.etPasswordRegister.apply {
                    requestFocus()
                    error = validation.password.message
                }
            }
        }



    }
}


        super.onViewCreated(view, savedInstanceState)
    }


    fun validateEmail(){

        binding.apply {
            etEmailRegister.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isEmpty() || !isValidEmail(s.toString())){
                        etEmailRegister.error = "Please enter a valid email address"
                    }
                }


            })
        }
    }

    fun validatePassword(){

        binding.apply {
            etPasswordRegister.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().isEmpty() || !isValidPassword(s.toString())){
                     etPasswordRegister.error = "At least 8 characters with at least one uppercase letter, one lowercase letter, and one digit"
                    }
                }



            })
        }
    }
    private fun isValidEmail(email:String): Boolean {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Add your password validation rules here
        // Example: At least 8 characters with at least one uppercase letter, one lowercase letter, and one digit
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }
}