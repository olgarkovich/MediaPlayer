package com.example.mediaplayer.json

import com.example.mediaplayer.model.Track

interface TrackCatalog {

    fun getTrackCatalog(): List<Track>
}