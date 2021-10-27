package com.example.mediaplayer.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mediaplayer.model.Track
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    var player: SimpleExoPlayer? = null
    var currentIndex = 0
    var isPlaying = true
    var trackList = emptyList<Track>()
    private var playWhenReady = true
    private var playbackPosition = 0L

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    fun getMediaPlayer(trackList: List<Track>) = initPlayer(trackList)

    private fun initPlayer(trackList: List<Track>): SimpleExoPlayer {
        player = SimpleExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentIndex, playbackPosition)
                exoPlayer.prepare()

                trackList.forEach {
                    val mediaItem = MediaItem.fromUri(it.trackUrl)
                    exoPlayer.addMediaItem(mediaItem)
                }
            }
        this.trackList = trackList
        return player as SimpleExoPlayer
    }

    fun getCurrentTrack(): Track {
        return trackList[currentIndex]
    }

    fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentIndex = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    fun next() {
        player?.seekToNext()
        currentIndex++
        isPlaying = true
        player?.playWhenReady = true
    }

    fun prev() {
        player?.seekToPrevious()
        currentIndex--
        isPlaying = true
        player?.playWhenReady = true
    }

    fun play() {
        isPlaying = !isPlaying
        player?.playWhenReady = isPlaying
    }
}