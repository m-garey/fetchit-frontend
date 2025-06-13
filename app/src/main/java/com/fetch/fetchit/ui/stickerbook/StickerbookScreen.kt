package com.fetch.fetchit.ui.stickerbook

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fetch.fetchit.R
import com.fetch.fetchit.feature.state.SectionUiState
import com.fetch.fetchit.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerbookScreen(
    state: StoreStarsUiState,
    onSubmit: (String) -> Unit = {},
    celebrationEvent: Pair<String, Int>? = null,
) {

    var celebration by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var showFirstStarSheet by remember { mutableStateOf(false) }
    var firstStarShown by remember { mutableStateOf(false) }
    val sampleImages = listOf(
        R.mipmap.grocerystore_foreground,
        R.mipmap.hardwarestore_foreground,
        R.mipmap.restaurant_foreground,
    )

    LaunchedEffect(celebrationEvent) {
        if (celebrationEvent != null) {
            celebration = celebrationEvent
        }
    }

    // Detect first star earned and trigger celebratory sheet once
    val totalStars = state.groceryStars + state.hardwareStars + state.restaurantStars

    LaunchedEffect(totalStars) {
        if (totalStars == 1 && !firstStarShown) {
            showFirstStarSheet = true
            firstStarShown = true
        }
    }

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
        topBar = { StickerbookTopBar() },
        bottomBar = { StickerbookBottomBar(onSubmit = onSubmit) },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(sections) {
                Section(it)
            }
        }
    }

    if (showFirstStarSheet) {
        CelebrationBottomSheet(
            onDismiss = { showFirstStarSheet = false },
            title = "Congratulations! You earned your first star!",
            subtitle = "Get four more to get a bronze sticker",
            topContent = {
                Text(
                    text = "🌟",
                    fontSize = 96.sp,
                )
            },
        )
    } else if (celebration != null) {
        val (categoryCelebrated, starsCelebrated) = celebration!!

        val medallionRes = when (starsCelebrated) {
            5 -> R.drawable.bronze
            10 -> R.drawable.silver
            15 -> R.drawable.gold
            else -> R.drawable.bronze
        }

        val logoRes = when (categoryCelebrated) {
            Constants.grocery.name -> R.mipmap.grocerystore_foreground
            Constants.hardware.name -> R.mipmap.hardwarestore_foreground
            else -> R.mipmap.restaurant_foreground
        }

        CelebrationBottomSheet(
            onDismiss = { celebration = null },
            title = "Nice! You earned a $categoryCelebrated sticker!",
            subtitle = "Great job collecting $starsCelebrated stars!",
            topContent = {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = medallionRes),
                        contentDescription = null,
                        modifier = Modifier.size(180.dp),
                        contentScale = ContentScale.Fit,
                    )

                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(185.dp)
                            .offset(y = 3.dp, x = (-1).dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerbookTopBar() {
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
}

@Composable
fun StickerbookBottomBar(onSubmit: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
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
}

// Reusable bottom-sheet composable for sticker / star celebrations
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CelebrationBottomSheet(
    onDismiss: () -> Unit,
    title: String,
    subtitle: String,
    topContent: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            topContent()

            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }

            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismiss) {
                Text("Awesome!")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun StickerbookPreview2() {
    StickerbookScreen(
        state = StoreStarsUiState(groceryStars = 2, hardwareStars = 14, restaurantStars = 7),
    )
}
