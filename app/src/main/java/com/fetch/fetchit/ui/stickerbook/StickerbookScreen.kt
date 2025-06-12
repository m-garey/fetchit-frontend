package com.fetch.fetchit.ui.stickerbook

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fetch.fetchit.R
import com.fetch.fetchit.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerbookScreen(
    state: StoreStarsUiState,
    onSubmit: (String) -> Unit = {},
) {
    val sampleImages = listOf(
        R.mipmap.grocerystore_foreground,
        R.mipmap.hardwarestore_foreground,
        R.mipmap.restaurant_foreground,
    )

    val sections = listOf(
        SectionUiState(
            title = "Grocery Stores",
            storeType = Constants.grocery.name,
            stars = state.groceryStars,
            imageResId = sampleImages[0],
        ),
        SectionUiState(
            title = "Hardware Stores",
            storeType = Constants.hardware.name,
            stars = state.hardwareStars,
            imageResId = sampleImages[1],
        ),
        SectionUiState(
            title = "Restaurants",
            storeType = Constants.restaurant.name,
            stars = state.restaurantStars,
            imageResId = sampleImages[2],
        ),
    )



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Stickerbook",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                    )
                },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            ) {
                Button(onClick = { onSubmit(Constants.grocery.name) }, shape = RoundedCornerShape(50)) {
                    Text("Grocery")
                }
                Button(onClick = { onSubmit(Constants.hardware.name) }, shape = RoundedCornerShape(50)) {
                    Text("Hardware")
                }
                Button(onClick = { onSubmit(Constants.restaurant.name) }, shape = RoundedCornerShape(50)) {
                    Text("Food")
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(sections) { section ->
                Section(section)
            }
        }
    }

    // Bottom sheet has been replaced by direct increment buttons, so no additional UI here.
}

private data class SectionUiState(
    val title: String,
    val storeType: String,
    val stars: Int,
    val imageResId: Int,
)

@Composable
private fun Section(
    state: SectionUiState,
    modifier: Modifier = Modifier,
) {
    val maxPerSticker = 5
    val starCounts = mutableListOf<Int>()
    var remaining = state.stars.coerceAtLeast(0)
    repeat(3) {
        val count = minOf(maxPerSticker, remaining)
        starCounts.add(count)
        remaining -= count
    }

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = state.title, style = MaterialTheme.typography.titleMedium)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(3) { index ->
                StickerItem(
                    imageResId = state.imageResId,
                    starCount = starCounts[index],
                    position = index,
                    modifier = Modifier.width(110.dp),
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun StickerItem(
    imageResId: Int,
    starCount: Int,
    position: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.large, clip = false)
            .background(color = if (starCount > 0) Color.White else Color.LightGray, shape = MaterialTheme.shapes.large)
    ) {
        val gradientColors = when (position) {
            0 -> listOf(Color(0xFFFD9C0C), Color(0xFFD27D04))
            1 -> listOf(Color(0xFFDADDED), Color(0xFFCCCEDB))
            else -> listOf(Color(0xFFFFED4E), Color(0xFFFFCD4E))
        }

        val gradient = Brush.linearGradient(
            colors = gradientColors,
            start = Offset.Zero,
            end = Offset.Infinite,
        )

        val boxBaseModifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)

        Box(
            modifier = if (starCount > 0) {
                boxBaseModifier.background(brush = gradient)
            } else {
                boxBaseModifier
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
                .padding(start= 8.dp, end = 8.dp, bottom = 8.dp),
        ) {
            val starSpacing = 2.dp
            val starSize = (maxWidth - starSpacing.times(4f)) / 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(starSpacing, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(5) { idx ->
                    val filled = idx < starCount
                    Box(modifier = Modifier.size(starSize)) {
                        // Draw stroke using an outlined star behind the filled/empty star
                        if (starCount > 0) {
                            Icon(
                                imageVector = Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.matchParentSize(),
                            )
                        }

                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (filled) Color.Yellow else Color.LightGray,
                            modifier = Modifier
                                .matchParentSize()
                                .padding(1.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun StickerbookPreview2() {
    StickerbookScreen(
        state = StoreStarsUiState(groceryStars = 2, hardwareStars = 4, restaurantStars = 7),
    )
}
