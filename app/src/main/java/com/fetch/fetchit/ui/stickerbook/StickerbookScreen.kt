package com.fetch.fetchit.ui.stickerbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.fetch.fetchit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerbookScreen() {
    val sampleImages = listOf(
        R.mipmap.grocerystore_foreground,
        R.mipmap.hardwarestore_foreground,
        R.mipmap.restaurant_foreground,
    )

    val sections = List(5) { index ->
        SectionUiState(
            title = "Section ${index + 1}",
            itemCount = 10,
            imageResId = sampleImages[index % sampleImages.size]
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Stickerbook", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(sections) { section ->
                Section(section)
            }
        }
    }
}

private data class SectionUiState(
    val title: String,
    val itemCount: Int,
    val imageResId: Int,
)

@Composable
private fun Section(state: SectionUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = state.title, style = MaterialTheme.typography.titleMedium)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.itemCount) { _ ->
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            color = Color.LightGray,
                            shape = MaterialTheme.shapes.large,
                        ),
                ) {
                    Image(
                        painter = painterResource(id = state.imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StickerbookScreenPreview() {
    StickerbookScreen()
}
