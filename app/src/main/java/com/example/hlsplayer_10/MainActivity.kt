package com.example.hlsplayer_10

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.example.hlsplayer_10.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val handler = Handler()
    private var player: ExoPlayer? = null

    //    private var exoPlayer: ExoPlayer? = null
    var mBinding: ActivityMainBinding? = null
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        initializePlayer()


        mBinding?.playHls?.setOnClickListener {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        var link = mBinding?.txtHlsLink?.text.toString()
        link = "http://3.110.171.248:90/hls/test-stream.m3u8"
        val uri = Uri.parse(link)
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                mBinding?.videoView?.player = exoPlayer
//                val mediaItem = MediaItem.fromUri(uri)
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
//                exoPlayer.playWhenReady = playWhenReady
//                exoPlayer.prepare()

                val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(this)
                val dataSourceFactory: DataSource.Factory =
                    DefaultDataSource.Factory(this).setTransferListener(bandwidthMeter)
                val audioSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
                exoPlayer.setMediaSource(audioSource)
                exoPlayer.prepare()
                exoPlayer.setPlayWhenReady(true)
            }


    }

//    private fun initialisePlayer() {
//        if (exoPlayer == null) {
//            exoPlayer = ExoPlayer.Builder(applicationContext)
//               .build()
//        }
//    }

//    private fun playAudio() {
//        val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(this)
//        val dataSourceFactory: DataSource.Factory =
//            DefaultDataSource.Factory(this).setTransferListener(bandwidthMeter)
//        val audioSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
//            .setLoadErrorHandlingPolicy(CustomPolicy())
//            .createMediaSource(MediaItem.fromUri("my-hls-master-url"))
//        exoPlayer?.setMediaSource(audioSource)
//        exoPlayer?.prepare()
//        exoPlayer?.setPlayWhenReady(true)
//    }

//    private fun releasePlayer() {
//        player?.let { exoPlayer ->
//            playbackPosition = exoPlayer.currentPosition
//            mediaItemIndex = exoPlayer.currentMediaItemIndex
//            playWhenReady = exoPlayer.playWhenReady
//            exoPlayer.release()
//        }
//        player = null
//    }

//    private fun play() {
//        player = ExoPlayerFactory.newSimpleInstance(
//            DefaultRenderersFactory(this), DefaultTrackSelector(), DefaultLoadControl()
//        )
////        val playerView = findViewById<SimpleExoPlayerView>(R.id.video_view)
////        var link = mBinding?.txtHlsLink?.text.toString()
//        var link = "http://3.110.171.248:90/hls/test-stream.m3u8"
//        val uri = Uri.parse(link)
//
//        mBinding?.playerView?.player = player
//
//        val dataSourceFactory = DefaultDataSourceFactory(this, "user-agent")
//        val mediaSource = HlsMediaSource(uri, dataSourceFactory, handler, null)
//
//        player?.prepare(mediaSource)
//        player?.playWhenReady = true
//    }

    private fun stop() {
        player?.playWhenReady = false
        player?.release()
    }

    private fun retry() {
        stop()
//        play()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }
}