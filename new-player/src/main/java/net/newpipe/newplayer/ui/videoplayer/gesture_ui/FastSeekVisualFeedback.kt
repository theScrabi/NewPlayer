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

package net.newpipe.newplayer.ui.videoplayer.gesture_ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import net.newpipe.newplayer.R
import net.newpipe.newplayer.ui.theme.VideoPlayerTheme
import net.newpipe.newplayer.ui.videoplayer.SEEK_ANIMATION_DURATION_IN_MS

@Composable
fun FastSeekVisualFeedback(modifier: Modifier = Modifier, seconds: Int, backwards: Boolean) {

    val contentDescription = String.format(
        if (backwards) {
            "Fast seeking backward by %d seconds."
            //stringResource(id = R.string.fast_seeking_backward)
        } else {
            "Fast seeking forward by %d seconds."
            //stringResource(id = R.string.fast_seeking_forward)
        }, seconds
    )

    val infiniteTransition = rememberInfiniteTransition()

    val animatedColor1 by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = SEEK_ANIMATION_DURATION_IN_MS
                Color.White.copy(alpha = 1f) at 0 with LinearEasing
                Color.White.copy(alpha = 0f) at SEEK_ANIMATION_DURATION_IN_MS with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = "Arrow1 animation"
    )

    val animatedColor2 by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = SEEK_ANIMATION_DURATION_IN_MS
                Color.White.copy(alpha = 1f / 3f) at 0 with LinearEasing
                Color.White.copy(alpha = 0f) at SEEK_ANIMATION_DURATION_IN_MS / 3 with LinearEasing
                Color.White.copy(alpha = 1f) at SEEK_ANIMATION_DURATION_IN_MS / 3 + 1 with LinearEasing
                Color.White.copy(alpha = 2f / 3f) at SEEK_ANIMATION_DURATION_IN_MS with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = "Arrow2 animation"
    )

    val animatedColor3 by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = SEEK_ANIMATION_DURATION_IN_MS
                Color.White.copy(alpha = 2f / 3f) at 0 with LinearEasing
                Color.White.copy(alpha = 0f) at 2 * SEEK_ANIMATION_DURATION_IN_MS / 3 with LinearEasing
                Color.White.copy(alpha = 1f) at 2 * SEEK_ANIMATION_DURATION_IN_MS / 3 + 1 with LinearEasing
                Color.White.copy(alpha = 2f / 3f) at SEEK_ANIMATION_DURATION_IN_MS with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = "Arrow3 animation"
    )


    //val secondsString = stringResource(id = R.string.seconds)
    val secondsString = "Seconds"

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            SeekerIcon(
                backwards = backwards,
                description = contentDescription,
                color = if (backwards) animatedColor3 else animatedColor1
            )
            SeekerIcon(
                backwards = backwards,
                description = contentDescription,
                color = animatedColor2
            )
            SeekerIcon(
                backwards = backwards,
                description = contentDescription,
                color = if (backwards) animatedColor1 else animatedColor3
            )
        }
        Text(text = "$seconds $secondsString")
    }

}


@Composable
private fun SeekerIcon(backwards: Boolean, description: String, color: Color) {
    Icon(
        modifier = if (backwards) {
            Modifier.scale(-1f, 1f)
        } else {
            Modifier
        },
        tint = color,
        painter = painterResource(id = R.drawable.ic_play_seek_triangle),
        contentDescription = description
    )
}

@Preview(device = "spec:width=1080px,height=600px,dpi=440,orientation=landscape")
@Composable
fun FastSeekVisualFeedbackPreviewBackwards() {
    VideoPlayerTheme {
        Surface(modifier = Modifier.wrapContentSize(), color = Color.Black) {
            FastSeekVisualFeedback(seconds = 10, backwards = true)
        }
    }
}

@Preview(device = "spec:width=1080px,height=600px,dpi=440,orientation=landscape")
@Composable
fun FastSeekVisualFeedbackPreview() {
    VideoPlayerTheme {
        Surface(modifier = Modifier.wrapContentSize(), color = Color.Black) {
            FastSeekVisualFeedback(seconds = 10, backwards = false)
        }
    }
}