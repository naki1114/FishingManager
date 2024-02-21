package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.Comment
import com.example.fishingmanager.databinding.FeedCommentItemBinding

class FeedCommentAdapter(feedCommentList : ArrayList<Comment>) : RecyclerView.Adapter<FeedCommentAdapter.ViewHolder>() {

    lateinit var binding : FeedCommentItemBinding
    private var feedCommentList : ArrayList<Comment> = feedCommentList


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {

        binding = FeedCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        holder.onBind(feedCommentList[position])

    } // onBindViewHolder()


    override fun getItemCount() : Int {

        return feedCommentList.size

    } // getItemCount()

    class ViewHolder(val binding : FeedCommentItemBinding, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(comment : Comment) {

            binding.comment = comment

            binding.feedViewItemWriterTextView.text = comment.nickname
            binding.feedViewItemCommentTextView.text = comment.content

        } // onBind()

    } // ViewHolder

    class ItemClickListener(val clickListener : (comment : Comment) -> Unit) {

        fun onClick(comment : Comment) = clickListener(comment)

    } // ItemClickListener

}