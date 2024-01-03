package com.example.fishingmanager.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingmanager.Data.ConditionWeather

class ConditionWeatherAdapter : RecyclerView.Adapter<ConditionWeatherAdapter.ViewHolder>() {

    val TAG = "ConditionWeatherAdapter"

    lateinit var weatherList : ArrayList<ConditionWeather>
    lateinit var binding : ConditionWeatherItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ConditionWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    } // onCreateViewHolder()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(weatherList[position])

    } // onBindViewHolder()


    override fun getItemCount(): Int {

        return weatherList.size

    } // getItemCount()


    fun setItem(item : ArrayList<ConditionWeather>) {

        weatherList = item
        notifyDataSetChanged()

    } // setItem()


    class ViewHolder(val binding : ConditionWeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(weatherList: ConditionWeather) {

            binding.weatherItem = weatherList

            binding.conditionWeatherSkyImage.setImageResource(weatherList.skyImage)

        }

    } // ViewHolder

}