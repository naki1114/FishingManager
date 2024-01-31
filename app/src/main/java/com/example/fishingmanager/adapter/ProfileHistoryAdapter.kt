package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.History
import com.example.fishingmanager.databinding.ProfileHistoryItemBinding

class ProfileHistoryAdapter : RecyclerView.Adapter<ProfileHistoryAdapter.ViewHolder>() {

    lateinit var binding: ProfileHistoryItemBinding
    lateinit var historyList : ArrayList<History>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ProfileHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

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


    class ViewHolder(val binding : ProfileHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(history : History) {

            binding.history = history
            binding.profileHistoryItemFishImage.setImageResource(history.fishIconImage)

        } // onBind()

    } // ViewHolder



}