package com.example.storeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storeapp.databinding.CartProductItemBinding
import com.example.storeapp.models.CartProduct
import com.example.storeapp.models.Product

class CartAdapter:RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    class MyViewHolder (val binding :CartProductItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(cartProduct: CartProduct){
            Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.cartProductIV)
            if(cartProduct.product.offerPercentage != null){
                val discount =cartProduct.product.price * cartProduct.product.offerPercentage
                val priceAfterDiscount = cartProduct.product.price - discount
                binding.cartProductPriceTV.text = priceAfterDiscount.toString() + "$"
            }else{
                binding.cartProductPriceTV.text = cartProduct.product.price.toString() + " $"
            }

            binding.cartProductNameTV.text= cartProduct.product.name
            binding.productQuantityTv.text = cartProduct.productQuantity.toString()

         /*   binding.imagePlus.setOnClickListener {
                binding.productQuantityTv.text = (cartProduct.productQuantity +1 ).toString()
            }
            binding.imageMinus.setOnClickListener {
                val newProductQuantity = cartProduct.productQuantity -1
                binding.productQuantityTv.text = newProductQuantity.toString()
                if (newProductQuantity == 0 || newProductQuantity == -1){
                    binding.productQuantityTv.text = "0"
                }
            }*/

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
        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(cartProduct)
        }
        holder.binding.imagePlus.setOnClickListener {
            onPlusClickListener?.invoke(cartProduct)
        }
        holder.binding.imageMinus.setOnClickListener {
            onMinusClickListener?.invoke(cartProduct)
        }

    }

    private val differCallback = object :DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
           return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean{
            return oldItem == newItem
        }

    }
     var onProductClickListener :((CartProduct) -> Unit)? = null
    var onPlusClickListener :((CartProduct) -> Unit)? = null
     var onMinusClickListener :((CartProduct) -> Unit)? = null

   /* fun onItemClickListener (listener :((CartProduct) -> Unit) ){
        onItemClickListener = listener
    }*/
    val differ = AsyncListDiffer(this,differCallback)


}