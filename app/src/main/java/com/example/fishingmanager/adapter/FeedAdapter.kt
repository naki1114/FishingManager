package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.FeedItemBinding
import java.text.SimpleDateFormat

class FeedAdapter(val itemClickListener : ItemClickListener, feedList : ArrayList<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    lateinit var binding : FeedItemBinding
    var feedList : ArrayList<Feed> = feedList


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {

        binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        holder.onBind(feedList[position])

    } // onBindViewHolder()


    override fun getItemCount() : Int {

        return feedList.size

    } // getItemCount()

    class ViewHolder(val binding : FeedItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(feed : Feed) {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            binding.feed = feed
            binding.clickListener = itemClickListener

            binding.feedItemTitleTextView.text = feed.title
            binding.feedItemWriterTextView.text = feed.nickname
            binding.feedItemViewCountTextView.text = "조회수 ${feed.viewCount}"
            binding.feedItemWriteDateTextView.text = dateFormat.format(feed.date.toLong()).toString()

            if (feed.feedImage == null) {

                binding.feedItemImageView.visibility = View.INVISIBLE

            }

        } // onBind()

    } // ViewHolder

    class ItemClickListener(val clickListener : (feed : Feed) -> Unit) {

        fun onClick(feed : Feed) = clickListener(feed)

    } // ItemClickListener

}