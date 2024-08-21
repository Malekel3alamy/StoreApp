package com.example.storeapp.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.storeapp.R
import com.example.storeapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccessoryFragment : BaseCategoryFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        super.baseCategoryViewModel.apply {
            getAccessories()
            lifecycleScope.launch {
                accessories.collect{
                    when(it){
                        is Resources.Error -> {
                            super.hideLoading()
                            Toast.makeText(requireContext(),"Sorry Couldn't Fetch Data ", Toast.LENGTH_LONG).show()
                        }
                        is Resources.Loading -> {
                            super.showLoading()
                        }
                        is Resources.Success -> {
                            hideLoading()
                            if(it.data!!.isNotEmpty()){
                                super.specialProductsAdapter.differ.submitList(it.data)

                            }
                        }
                        is Resources.UnSpecified -> Unit
                    }
                }
            }
        }
        // Item click Listener
        super.setupItemClickListener()
    }
}