/* NewPlayer
 *
 * @author Christian Schabesberger
 *
 * Copyright (C) NewPipe e.V. 2024 <code(at)newpipe-ev.de>
 *
 * NewPlayer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPlayer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPlayer.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.newpipe.newplayer.ui

import android.content.pm.ActivityInfo
import android.view.SurfaceView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import net.newpipe.newplayer.model.VideoPlayerViewModel
import net.newpipe.newplayer.model.VideoPlayerViewModelImpl
import net.newpipe.newplayer.ui.theme.VideoPlayerTheme
import net.newpipe.newplayer.utils.LockScreenOrientation

@Composable
fun VideoPlayerUI(
    viewModel: VideoPlayerViewModel?,
) {
    if (viewModel == null) {
        VideoPlayerLoadingPlaceholder()
    } else if (viewModel.player == null || viewModel.uiState.collectAsState().value.contentRatio == 0.0F) {
        VideoPlayerLoadingPlaceholder(viewModel.uiState.collectAsState().value.uiRatio)
    } else {
        val uiState by viewModel.uiState.collectAsState()

        var lifecycle by remember {
            mutableStateOf(Lifecycle.Event.ON_CREATE)
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        // Prepare stuff for the SurfaceView to which the video will be rendered
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }


        // Handle Fullscreen/Embedded view transition
        if (uiState.fullscreen) {
            BackHandler {
                //closeFullscreen(viewModel, activity!!)
            }
        }

        LaunchedEffect(key1 = uiState.fullscreen) {
            println("gurken launch fullscreen: ${uiState.fullscreen}")
        }

        // Set Screen Rotation
        if (uiState.fullscreen) {
            if (uiState.contentRatio < 1) {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            } else {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            }
        }

        // Set UI
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(uiState.uiRatio),
            color = Color.Black
        ) {
            Box(contentAlignment = Alignment.Center){
                AndroidView(
                    modifier = Modifier.also { modifier ->
                        when (uiState.contentFitMode) {
                            ContentFitMode.FILL -> modifier.fillMaxSize()
                            ContentFitMode.FIT_INSIDE -> if (uiState.contentRatio < uiState.uiRatio) {
                                modifier.fillMaxHeight().aspectRatio(uiState.contentRatio)
                            } else if (uiState.uiRatio < uiState.contentRatio) {
                                modifier.fillMaxWidth().aspectRatio(uiState.contentRatio)
                            }
                            ContentFitMode.ZOOM -> if(uiState.uiRatio < uiState.contentRatio) {
                                modifier.fillMaxHeight().aspectRatio(uiState.contentRatio)
                            } else if (uiState.contentRatio < uiState.uiRatio) {
                                modifier.fillMaxWidth().aspectRatio(uiState.contentRatio)
                            }
                        }
                    },
                    factory = { context ->
                        SurfaceView(context).also { view ->
                            viewModel.player?.setVideoSurfaceView(view)
                        }
                    }, update = { view ->
                        when (lifecycle) {
                            Lifecycle.Event.ON_RESUME -> {
                                viewModel.player?.setVideoSurfaceView(view)
                            }

                            else -> Unit
                        }
                    })

                VideoPlayerControllerUI(
                    isPlaying = uiState.playing,
                    fullscreen = uiState.fullscreen,
                    play = viewModel::play,
                    pause = viewModel::pause,
                    prevStream = viewModel::prevStream,
                    nextStream = viewModel::nextStream,
                    switchToFullscreen = viewModel::switchToFullscreen,
                    switchToEmbeddedView = viewModel::switchToEmbeddedView
                )
            }
        }
    }
}

@Preview(device = "spec:width=1080px,height=700px,dpi=440,orientation=landscape")
@Composable
fun PlayerUIPreviewEmbeded() {
    VideoPlayerTheme {
        VideoPlayerUI(viewModel = VideoPlayerViewModelImpl.dummy)
    }
}