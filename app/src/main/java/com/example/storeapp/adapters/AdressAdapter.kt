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
          fun bind(address: ProductAddress){
              binding.apply {
                  buttonAddress.text = address.addressTitle

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
    var onClick : ((ProductAddress,Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var selectedAddress = false
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
          val address = differ.currentList[position]
        Log.d("Address",address.addressTitle)


        holder.bind(address)



        holder.binding.buttonAddress.setOnClickListener {
           if (selectedAddress ){
               //it.background = ColorDrawable(holder.itemView.context.resources.getColor(R.color.g_white))
               holder.binding.buttonAddress.setBackgroundColor(holder.itemView.resources.getColor(R.color.red))
               holder.binding.buttonAddress.setTextColor(holder.itemView.context.resources.getColor(R.color.g_blue))
               selectedAddress =false
               onClick?.invoke(address,selectedAddress)


           }else{

              // notifyItemChanged(position)
               //= ColorDrawable(holder.itemView.context.resources.getColor(R.color.g_blue))
               holder.binding.buttonAddress.setBackgroundColor(holder.itemView.resources.getColor(R.color.g_blue))
               holder.binding.buttonAddress.setTextColor(holder.itemView.context.resources.getColor(R.color.g_white))
               selectedAddress = true
             //  notifyItemChanged(position)
               onClick?.invoke(address,selectedAddress)
           }

        }
    }
}