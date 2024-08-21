package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.storeapp.databinding.BillingProductsRvItemBinding
import com.example.storeapp.models.CartProduct
import com.example.storeapp.utils.CalculatePriceAfterDiscount

class BillingProductsAdapter :RecyclerView.Adapter<BillingProductsAdapter.BillingViewHolder>() {
    class BillingViewHolder(val binding: BillingProductsRvItemBinding) : ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvProductCartPrice.text = (CalculatePriceAfterDiscount(cartProduct.product.price,cartProduct.product.offerPercentage) * cartProduct.productQuantity ).toString() + "$"
                tvBillingProductQuantity.text = cartProduct.productQuantity.toString()

            }
        }

    }

    private val differCallback = object :DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
           return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)
    val onClick : ((CartProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder((BillingProductsRvItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
              holder.bind(cartProduct)
    }
}