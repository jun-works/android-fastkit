package com.junworks.fastkit.sample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.junworks.fastkit.media.CommonPlayerManager
import com.junworks.fastkit.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var playerManager: CommonPlayerManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        playerManager = CommonPlayerManager(this)
        playerManager.attachView(binding.playerView)
        playerManager.initialize()


        // 테스트용 영상 재생
        playerManager.play("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
    }

    override fun onStop() {
        super.onStop()
        playerManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}