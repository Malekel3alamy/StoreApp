package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.ProductColorItemBinding
import com.example.storeapp.databinding.ProductRvItemBinding

class ProductColorsAdapter:RecyclerView.Adapter<ProductColorsAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding:ProductColorItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(color:String){
            binding.colorNameTv.text = color
        }

    }
    val differCallback = object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ProductColorItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          val color = differ.currentList[position]
        holder.bind(color)
    }
}