package com.example.storeapp.ui.fragments.shopping

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.adapters.SpecialProductsAdapter
import com.example.storeapp.databinding.FragmentSearchBinding
import com.example.storeapp.utils.Resources
import com.example.storeapp.utils.showNavView
import com.example.storeapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding : FragmentSearchBinding
    private val allOrdersViewModel by viewModels<AllOrdersViewModel>()


    private val searchAdapter  = SpecialProductsAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        showNavView()

        hidePR()

        setupSearchRecyclerView()


        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.searchIcon.setOnClickListener {
                    showPR()
                    s?.let {
                        if(s.toString().isNotEmpty()){
                          allOrdersViewModel.searchProduct(s.toString())
                            lifecycleScope.launch {
                                allOrdersViewModel.searchResult.collectLatest {
                                    when(it){
                                        is Resources.Error -> {
                                            hidePR()
                                            Toast.makeText(requireContext()," Error : ${it.message}",Toast.LENGTH_SHORT).show()
                                        }
                                        is Resources.Loading -> {
                                            showPR()

                                        }
                                        is Resources.Success -> {
                                            hidePR()
                                            searchAdapter.differ.submitList(it.data)

                                        }
                                        is Resources.UnSpecified -> {

                                            hidePR()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        })

        binding.imageCloseSearch.setOnClickListener {
            findNavController().navigateUp()
        }
        searchAdapter.onItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("Product",it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_productInfoFragment,bundle)
        }
    }



    private fun  showPR(){
       binding.searchBG.visibility = View.INVISIBLE
        binding.searchPR.visibility = View.VISIBLE
        binding.searchRV.visibility = View.VISIBLE

    }
    private fun  hidePR(){
        binding.searchPR.visibility = View.INVISIBLE
    }

    private fun setupSearchRecyclerView(){
        binding.searchRV.adapter = searchAdapter
        binding.searchRV.layoutManager = LinearLayoutManager(requireContext())
    }

}