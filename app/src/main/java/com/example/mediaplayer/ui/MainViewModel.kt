package com.example.mediaplayer.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mediaplayer.model.Track
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    fun getMediaPlayer(trackList: List<Track>) = initPlayer(trackList)

    private fun initPlayer(trackList: List<Track>): SimpleExoPlayer {
        player = SimpleExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()

                trackList.forEach {
                    val mediaItem = MediaItem.fromUri(it.trackUrl)
                    exoPlayer.addMediaItem(mediaItem)
                }
            }
        return player as SimpleExoPlayer
    }

    fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    fun next() {
        player?.seekToNext()
    }

    fun prev() {
        player?.seekToPrevious()
    }

    fun play() {
        player?.playWhenReady = this.playWhenReady
    }

    fun stop() {
        player?.playWhenReady = this.playWhenReady
    }


}