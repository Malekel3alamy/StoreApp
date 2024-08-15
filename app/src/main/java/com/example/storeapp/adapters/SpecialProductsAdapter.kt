package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.FragmentHomeBinding
import com.example.storeapp.databinding.SpecialRvItemBinding
import com.example.storeapp.models.Product
import com.bumptech.glide.Glide

class SpecialProductsAdapter :RecyclerView.Adapter<SpecialProductsAdapter.MyViewHolder>(){
    class MyViewHolder(private val binding: SpecialRvItemBinding):RecyclerView.ViewHolder(binding.root) {
        
                  fun bind(product: Product){


                      binding.apply {
                          Glide.with(itemView).load(product.images[0]).into(imageSpecialProduct)
                          tvAdName.text = product.name
                          tvAdPrice.text = "${product.price.toString()} $"

                      }
                  }
    }
    private var onItemClickListener :((Product) -> Unit)? = null

    fun onItemClickListener (listener :((Product) -> Unit) ){
        onItemClickListener = listener
    }
    
    val differCallBAck = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
           return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBAck)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SpecialRvItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                 val product = differ.currentList[position]
                  holder.bind(product)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                product?.let {product->
                    it(product)

                }
            }
        }

    }


}