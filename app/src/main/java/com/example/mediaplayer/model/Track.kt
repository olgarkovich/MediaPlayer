package com.example.mediaplayer.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Track(

    @Json(name = "title")
    val title: String,

    @Json(name = "artist")
    val artist: String,

    @Json(name = "bitmapUri")
    val coverUrl: String,

    @Json(name = "trackUri")
    val trackUrl: String,

    @Json(name = "duration")
    val duration: Int

)