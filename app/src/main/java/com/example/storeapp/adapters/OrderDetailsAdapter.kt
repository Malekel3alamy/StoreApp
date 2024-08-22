package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storeapp.databinding.BillingProductsRvItemBinding
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.order.Order
import com.example.storeapp.utils.CalculatePriceAfterDiscount

class OrderDetailsAdapter:RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding :BillingProductsRvItemBinding ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProduct){
            Glide.with(itemView).load(product.product.images[0]).into(binding.imageCartProduct)
          binding.tvProductCartName.text = product.product.name
            binding.tvProductCartPrice.text = (CalculatePriceAfterDiscount(product.product.price,product.product.offerPercentage) * product.productQuantity ).toString()
             binding.tvBillingProductQuantity.text = product.productQuantity.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = differ.currentList[position]
        holder.bind(product)


    }
    private val differCallback = object   : DiffUtil.ItemCallback<CartProduct> (){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

}