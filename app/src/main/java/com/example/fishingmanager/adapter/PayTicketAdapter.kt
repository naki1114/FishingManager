package com.example.fishingmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.data.PayTicket
import com.example.fishingmanager.databinding.PayTicketItemBinding

class PayTicketAdapter(val itemClickListener : ItemClickListener) : RecyclerView.Adapter<PayTicketAdapter.ViewHolder>() {

    lateinit var binding : PayTicketItemBinding
    lateinit var ticketList : ArrayList<PayTicket>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = PayTicketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, itemClickListener, parent.context)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(ticketList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return ticketList.size

    } // getItemCount()


    fun setItem(item : ArrayList<PayTicket>) {

        ticketList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : PayTicketItemBinding, val itemClickListener : ItemClickListener, val context : Context) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(payTicket: PayTicket) {

            binding.payTicket = payTicket
            binding.clickListener = itemClickListener

            binding.payIcon1Image.setImageResource(payTicket.icon1)
            binding.payIcon2Image.setImageResource(payTicket.icon2)

            if (payTicket.icon2 == 0) {

                binding.payIcon2Image.visibility = View.GONE
                binding.payIconSpace.visibility = View.GONE

            } else {

                binding.payIcon2Image.visibility = View.VISIBLE
                binding.payIconSpace.visibility = View.VISIBLE

            }

        }

    } // ViewHolder


    class ItemClickListener(val clickListener: (payTicket : PayTicket) -> Unit) {

        fun onClick(payTicket: PayTicket) = clickListener(payTicket)

    } // ItemClickListener

}