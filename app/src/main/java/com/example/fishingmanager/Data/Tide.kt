package com.example.fishingmanagerclone.Data

data class Tide(val result: Result) {

    data class Result(
        val data: List<Item>,
        val meta: Meta
    )

    data class Meta(
        val obs_post_id: String,
        val obs_last_req_cnt: String,
        val obs_lat: String,
        val obs_lon: String,
        val obs_post_name: String
    )

    data class Item(
        val tph_level: String,
        val tph_time: String,
        val hl_code: String
    )

}