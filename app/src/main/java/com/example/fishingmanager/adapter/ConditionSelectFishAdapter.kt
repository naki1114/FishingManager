package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.databinding.ConditionSelectFishItemBinding

class ConditionSelectFishAdapter(val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ConditionSelectFishAdapter.ViewHolder>() {

    val TAG = "ConditionSelectFishAdapter"

    lateinit var fishList : ArrayList<SelectFish>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ConditionSelectFishItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(fishList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return fishList.size

    } // getItemCount()


    fun setItem(item : ArrayList<SelectFish>) {

        fishList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ConditionSelectFishItemBinding, val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(fishList: SelectFish) {

            binding.fishItem = fishList
            binding.conditionSelectFishFishImage.setImageResource(fishList.fishImage)
            binding.clickListener = itemClickListener

        }

    } // ViewHolder


    class ItemClickListener(val clickListener : (selectFish: SelectFish) -> Unit) {
        fun onClick(selectFish: SelectFish) = clickListener(selectFish)
    }

}