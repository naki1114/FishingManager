package com.example.fishingmanager.Data

data class Index(val result : Result) {

    data class Result(
        val data : ArrayList<Item>,
        val meta : Meta
    )

    data class Meta(
        val typeName : String,
        val obs_last_req_cnt : String
    )
    data class Item(
        val time_type : String,
        val fish_name : String,
        val lat : String,
        val lon : String,
        val water_temp : String,
        val air_temp : String,
        val wind_speed:String,
        val wave_height : String,
        val name : String,
        val tide_time_score : String,
        val total_score : String,
        val date : String
    )

}