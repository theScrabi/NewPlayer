package net.newpipe.newplayer.model

import android.os.Bundle
import androidx.media3.common.Player
import kotlinx.coroutines.flow.MutableStateFlow
import net.newpipe.newplayer.NewPlayer
import net.newpipe.newplayer.ui.ContentScale

open class VideoPlayerViewModelDummy : VideoPlayerViewModel {
    override var newPlayer: NewPlayer? = null
    override val internalPlayer: Player? = null
    override val uiState = MutableStateFlow(VideoPlayerUIState.DEFAULT)
    override var minContentRatio = 4F / 3F
    override var maxContentRatio = 16F / 9F
    override var contentFitMode = ContentScale.FIT_INSIDE

    override fun initUIState(instanceState: Bundle) {
        println("dummy impl")
    }

    override fun addCallbackListener(listener: VideoPlayerViewModel.Listener) {
        println("dummy impl")
    }

    override fun play() {
        println("dummy impl")
    }

    override fun switchToEmbeddedView() {
        println("dummy impl")
    }

    override fun switchToFullscreen() {
        println("dummy impl")
    }

    override fun showUi() {
        println("dummy impl")
    }

    override fun hideUi() {
        println("dummy impl")
    }

    override fun seekPositionChanged(newValue: Float) {
        println("dymmy seekPositionChanged: newValue: ${newValue}")
    }

    override fun seekingFinished() {
        println("dummy impl")
    }

    override fun embeddedDraggedDown(offset: Float) {
        println("dymmy embeddedDraggedDown: offset: ${offset}")
    }

    override fun fastSeek(steps: Int) {
        println("dummy impl")
    }

    override fun finishFastSeek() {
        println("dummy impl")
    }

    override fun brightnessChange(changeRate: Float, currentValue: Float) {
        println("dummy impl")
    }

    override fun volumeChange(changeRate: Float) {
        println("dummy impl")
    }

    override fun pause() {
        println("dummy pause")
    }

    override fun prevStream() {
        println("dummy impl")
    }

    override fun nextStream() {
        println("dummy impl")
    }
}
