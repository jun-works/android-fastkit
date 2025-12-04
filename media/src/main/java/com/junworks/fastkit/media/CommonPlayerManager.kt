package com.junworks.fastkit.media

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlin.apply
import kotlin.collections.map
import kotlin.let
import androidx.core.net.toUri
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import kotlin.apply
import kotlin.collections.map

/**
 * 공통 비디오 플레이어 매니저
 * - ExoPlayer 인스턴스 관리
 * - 생명주기 처리
 * - 기본적인 재생/일시정지 기능 캡슐화
 */
class CommonPlayerManager(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var playerView: PlayerView? = null

    // 플레이어 설정
    data class Config(
        val autoPlay: Boolean = true,
        val repeatMode: Int = Player.REPEAT_MODE_OFF,
        val handleAudioFocus: Boolean = true
    )

    fun initialize(config: Config = Config()): CommonPlayerManager {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context)
                .setHandleAudioBecomingNoisy(true) // 이어폰을 뽑을 때 일시정지 처리
                .build()
                .apply {
                    playWhenReady = config.autoPlay
                    repeatMode = config.repeatMode
                }
        }
        // 이미 뷰가 설정되어 있다면 다시 연결
        playerView?.player = exoPlayer
        return this
    }

    // PlayerView 연결
    fun attachView(view: PlayerView) {
        this.playerView = view
        view.player = exoPlayer
    }

    // PlayerView 연결 해제 (뷰가 파괴될 때 등)
    fun detachView() {
        playerView?.player = null
        playerView = null
    }


    // URL 재생
    fun play(url: String) {
        play(url.toUri())
    }

    // Raw Resource ID 재생 (R.raw.sample_video)
    @OptIn(UnstableApi::class)
    fun play(resId: Int) {
        val uri = RawResourceDataSource.buildRawResourceUri(resId)
        play(uri)
    }

    // Uri 재생 (공통 로직)
    fun play(uri: Uri) {
        initialize()
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // URL 리스트 재생
    fun playList(urls: List<String>) {
        val mediaItems = urls.map { MediaItem.fromUri(Uri.parse(it)) }
        setMediaItemsAndPrepare(mediaItems)
    }

    // Uri 리스트 재생
    fun playListUris(uris: List<Uri>) {
        val mediaItems = uris.map { MediaItem.fromUri(it) }
        setMediaItemsAndPrepare(mediaItems)
    }

    private fun setMediaItemsAndPrepare(items: List<MediaItem>) {
        initialize()
        exoPlayer?.apply {
            setMediaItems(items)
            prepare()
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    /**
     * Activity/Fragment의 onStop 또는 onDestroy에서 호출하여 메모리 릭 방지
     */
    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        playerView?.player = null
        playerView = null
    }

    // 재생 중인지 확인
    fun isPlaying(): Boolean = exoPlayer?.isPlaying == true

    // Listener가 필요할 경우 외부로 노출
    fun addListener(listener: Player.Listener) {
        exoPlayer?.addListener(listener)
    }
}