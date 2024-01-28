package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishingmanager.data.History
import com.example.fishingmanager.databinding.CheckingfishHistoryItemBinding

class CheckingFishHistoryAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<CheckingFishHistoryAdapter.ViewHolder>() {

    lateinit var binding : CheckingfishHistoryItemBinding
    lateinit var historyList : ArrayList<History>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = CheckingfishHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(historyList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return historyList.size

    } // getItemCount()


    fun setItem(item : ArrayList<History>) {

        historyList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : CheckingfishHistoryItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(history : History) {

            binding.history = history
            binding.clickListener = itemClickListener

            Glide.with(context).load(history.fishImage).into(binding.checkingFishHistoryItemImage)

        } // onBind()

    } // ViewHolder


    class ItemClickListener(val clickListener : (history : History) -> Unit) {

        fun onClick(history: History) = clickListener(history)

    } // ItemClickListener

}