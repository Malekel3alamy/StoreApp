package com.example.storeapp.adapters

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.R
import com.example.storeapp.databinding.OrderItemBinding
import com.example.storeapp.models.order.Order
import com.example.storeapp.models.order.OrderStatus
import com.example.storeapp.models.order.getOrderStatus

class AllOrdersAdapter() : RecyclerView.Adapter<AllOrdersAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding :OrderItemBinding ):RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order){
            binding.tvOrderId.text = order.orderId.toString()
            binding.tvOrderDate.text = order.date
            binding.tvOrderStatus.text = order.orderStatus
            val colorDrawable =when(getOrderStatus(order.orderStatus)){
                OrderStatus.Canceled -> {itemView.resources.getColor(R.color.g_red)}
                OrderStatus.Confirmed -> {itemView.resources.getColor(R.color.g_green)}
                OrderStatus.Delivered -> {itemView.resources.getColor(R.color.g_blue)}
                OrderStatus.Ordered -> {itemView.resources.getColor(R.color.g_orange_yellow)}
                OrderStatus.Returned -> {itemView.resources.getColor(R.color.g_gray700)}
                OrderStatus.Shipped -> {itemView.resources.getColor(R.color.g_blue100)}
            }
            binding.imageOrderState.setImageDrawable(ColorDrawable(colorDrawable))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(OrderItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = differ.currentList[position]
       holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    private val differCallback = object :DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
    var onClick  :((Order) -> Unit)? = null
}