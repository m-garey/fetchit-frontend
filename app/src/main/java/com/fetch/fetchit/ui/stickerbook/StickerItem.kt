package com.fetch.fetchit.ui.stickerbook

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

/**
 * Individual sticker item displaying an image with gradient background and star rating.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StickerItem(
    imageResId: Int,
    starCount: Int,
    position: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.large,
                clip = false,
            )
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.large,
            ),
    ) {
        // Select gradient based on position within the row.
        val gradientColors = when (position) {
            0 -> listOf(Color(0xFFFD9C0C), Color(0xFFD27D04)) // orange
            1 -> listOf(Color(0xFFDADDED), Color(0xFFCCCEDB)) // gray-blue
            else -> listOf(Color(0xFFFFED4E), Color(0xFFFFCD4E)) // yellow
        }

        val gradient = Brush.linearGradient(
            colors = gradientColors,
            start = Offset.Zero,
            end = Offset.Infinite,
        )

        val boxBaseModifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))

        Box(
            modifier = if (starCount > 0) {
                boxBaseModifier.background(brush = gradient)
            } else {
                boxBaseModifier.background(Color.LightGray)
            },
        ) {
            if (starCount > 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        ) {
            val starSpacing = 2.dp
            val starSize = (maxWidth - starSpacing.times(3f)) / 5

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    starSpacing,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(5) { idx ->
                    val filled = idx < starCount

                    Box(modifier = Modifier.size(starSize)) {
                        // Outline/background star that is always visible
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.matchParentSize(),
                        )

                        // Filled star that animates in/out based on the filled state
                        val targetScale = if (filled) 1f else 0f
                        val scale by animateFloatAsState(
                            targetValue = targetScale,
                            animationSpec = tween(durationMillis = 250, easing = EaseInElastic),
                            label = "starScaleAnimation",
                        )

                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier
                                .matchParentSize()
                                .padding(1.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    alpha = scale
                                },
                        )
                    }
                }
            }
        }
    }
}
