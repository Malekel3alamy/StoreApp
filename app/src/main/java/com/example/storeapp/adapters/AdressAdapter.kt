package com.example.storeapp.adapters

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.storeapp.R
import com.example.storeapp.adapters.BestDealsAdapter.MyViewHolder
import com.example.storeapp.databinding.AddressRvItemBinding
import com.example.storeapp.databinding.BestDealsRvItemBinding
import com.example.storeapp.databinding.BillingProductsRvItemBinding
import com.example.storeapp.models.Product
import com.example.storeapp.models.ProductAddress

class AddressAdapter :Adapter<AddressAdapter.AddressViewHolder>() {
    class AddressViewHolder ( val binding : AddressRvItemBinding) : ViewHolder(binding.root){
          fun bind(address: ProductAddress,isSelected  :Boolean){
              binding.apply {
                  buttonAddress.text = address.addressTitle
                  if (isSelected){
                      buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                      buttonAddress.setTextColor(itemView.context.resources.getColor(R.color.g_white))
                  }else{
                      buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                      buttonAddress.setTextColor(itemView.context.resources.getColor(R.color.g_gray700))

                  }
              }
          }

    }
    private val differCallBAck = object : DiffUtil.ItemCallback<ProductAddress>() {
        override fun areItemsTheSame(oldItem: ProductAddress, newItem: ProductAddress): Boolean {
            return oldItem.userFullName == newItem.userFullName && oldItem.addressTitle == newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: ProductAddress, newItem: ProductAddress): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBAck)
    var onClick : ((ProductAddress) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
          val address = differ.currentList[position]
        Log.d("Address",address.addressTitle)


        holder.bind(address,selectedAddress==position)
        selectedAddress = position


        holder.binding.buttonAddress.setOnClickListener {
           if (selectedAddress >= 0){
               notifyItemChanged(position)
               selectedAddress = holder.adapterPosition
               notifyItemChanged(position)
               onClick?.invoke(address)

           }

        }
    }
}