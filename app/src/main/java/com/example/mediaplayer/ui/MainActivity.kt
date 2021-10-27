package com.example.mediaplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mediaplayer.R
import com.example.mediaplayer.appComponent
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.json.TrackCatalog
import com.example.mediaplayer.res.AppResources
import com.google.android.exoplayer2.util.Util
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

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

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initView()
        }
    }

    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24)) {
            initView()
        }
    }

    private fun initView() {
        viewModel.getMediaPlayer(trackCatalog.getTrackCatalog())
        renderData()
        setEnableControls()
        setListeners()
    }

    private fun renderData() {
        viewBinding.title.text = viewModel.getCurrentTrack().title
        viewBinding.artist.text = viewModel.getCurrentTrack().artist

        viewBinding.cover.load(viewModel.getCurrentTrack().coverUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_music_video_24)
            allowHardware(false)
        }
        renderButton()
    }

    private fun renderButton() {
        if (viewModel.isPlaying) {
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
            renderData()
            setEnableControls()
        }
        viewBinding.nextButton.setOnClickListener {
            viewModel.next()
            renderData()
            setEnableControls()
        }
        viewBinding.playButton.setOnClickListener {
            viewModel.play()
            renderButton()
        }
    }

    private fun setEnableControls() {
        viewBinding.prevButton.isEnabled = viewModel.currentIndex != 0
        viewBinding.nextButton.isEnabled = viewModel.currentIndex != viewModel.trackList.lastIndex
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