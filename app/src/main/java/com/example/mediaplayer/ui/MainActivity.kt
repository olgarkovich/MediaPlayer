package com.example.mediaplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.example.mediaplayer.R
import com.example.mediaplayer.appComponent
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.json.TrackCatalog
import com.example.mediaplayer.model.Track
import com.example.mediaplayer.res.AppResources
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var viewModel: MainViewModel

    private var currentIndex = 0
    private var isPlaying = true

    private var trackList = emptyList<Track>()

    @Inject
    lateinit var appResources: AppResources

    @Inject
    lateinit var trackCatalog: TrackCatalog

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        this.appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)

        trackList = trackCatalog.getTrackCatalog()
    }


    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initView()
            setListeners()
        }
    }

    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            initView()
            setListeners()
        }
    }

    private fun initView() {
        exoPlayer = viewModel.getMediaPlayer(trackList)
        currentIndex = exoPlayer?.currentWindowIndex ?: 0
        renderData()
        setEnableControls()
    }

    private fun renderData() {
        viewBinding.title.text = trackList[currentIndex].title
        viewBinding.artist.text = trackList[currentIndex].artist

        viewBinding.cover.load(trackList[currentIndex].coverUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_music_video_24)
            allowHardware(false)
        }
        renderButton()
    }

    private fun renderButton() {
        if (isPlaying) {
            viewBinding.playButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_baseline_pause_circle_filled_24
                )
            )
        } else {
            viewBinding.playButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_baseline_play_circle_filled_24
                )
            )
        }
    }

    private fun setListeners() {

        viewBinding.prevButton.setOnClickListener {
            viewModel.prev()
            isPlaying = true
            currentIndex--
            renderData()
            setEnableControls()
        }
        viewBinding.nextButton.setOnClickListener {
            viewModel.next()
            isPlaying = true
            currentIndex++
            renderData()
            setEnableControls()
        }
        viewBinding.playButton.setOnClickListener {
            if (isPlaying) {
                viewModel.stop()
            } else {
                viewModel.play()
            }
            isPlaying = !isPlaying
            renderButton()
        }
    }

    private fun setEnableControls() {
        viewBinding.prevButton.isEnabled = currentIndex != 0
        viewBinding.nextButton.isEnabled = currentIndex != trackList.lastIndex
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            viewModel.releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            viewModel.releasePlayer()
        }
    }
}