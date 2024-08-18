package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storeapp.databinding.CartProductItemBinding
import com.example.storeapp.models.CartProduct

class CartAdapter:RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    class MyViewHolder (val binding :CartProductItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(cartProduct: CartProduct){
            Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.cartProductIV)
            binding.cartProductPriceTV.text = cartProduct.product.price.toString() + "$"
            binding.cartProductNameTV.text= cartProduct.product.name
            binding.productQuantityTv.text = cartProduct.productQuantity.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }



    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
    }

    private val differCallback = object :DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
           return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean{
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)


}