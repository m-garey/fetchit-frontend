package com.fetch.fetchit.ui.stickerbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fetch.fetchit.feature.state.SectionUiState

/**
 * Displays a horizontal row (LazyRow) of three [StickerItem]s that visualise the number of
 * collected stars for a particular store type.
 */
@Composable
fun Section(
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
        modifier = modifier,
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
