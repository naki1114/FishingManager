package com.example.fishingmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.databinding.ProfileCollectionItemBinding

class ProfileCollectionAdapter : RecyclerView.Adapter<ProfileCollectionAdapter.ViewHolder>() {


    lateinit var binding: ProfileCollectionItemBinding
    lateinit var collectionList : ArrayList<Collection>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ProfileCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(collectionList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return collectionList.size

    } // getItemCount()


    class ViewHolder(val binding : ProfileCollectionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(collection: Collection) {



        } // onBind()

    } // ViewHolder


    class ItemClickListener(val clickListener : (collection : Collection) -> Unit) {

        fun onClick(collection : Collection) = clickListener(collection)

    } // ItemClickListner

}