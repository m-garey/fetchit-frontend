package com.fetch.fetchit.ui.stickerbook

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fetch.fetchit.R
import com.fetch.fetchit.utils.Constants
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerbookScreen(
    onSubmit: (String) -> Unit = {},
) {
    val sampleImages = listOf(
        R.mipmap.grocerystore_foreground,
        R.mipmap.hardwarestore_foreground,
        R.mipmap.restaurant_foreground,
    )

    val sections = List(3) { index ->
        SectionUiState(
            title = "Section ${index + 1}",
            itemCount = 3,
            imageResId = sampleImages[index % sampleImages.size]
        )
    }

    // Bottom sheet & button state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Stickerbook", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = { showSheet = true },
                    shape = RoundedCornerShape(50),
                ) {
                    Text("Scan a receipt")
                }
            }
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

    if (showSheet) {
        ReceiptScanBottomSheet(
            onDismiss = { showSheet = false },
            onSubmit = { store ->
                showSheet = false
                onSubmit(store)
            },
            sheetState = sheetState,
        )
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
                StickerItem(imageResId = state.imageResId)
            }
        }
    }
}

@Composable
private fun StickerItem(
    imageResId: Int,
    modifier: Modifier = Modifier,
) {
    val boxSize = 150.dp
    val starSize = 24.dp

    Box(
        modifier = modifier
            .size(boxSize)
            .background(
                color = Color.LightGray,
                shape = MaterialTheme.shapes.large,
            ),
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Overlay arc of stars
        val density = LocalDensity.current
        val starOffsets = remember(density) {
            with(density) {
                val boxSizePx = boxSize.toPx()
                val starSizePx = starSize.toPx()
                val radiusPx = (boxSizePx - starSizePx) / 2f
                val centerPx = boxSizePx / 2f - starSizePx / 2f

                val start = 1.0/4.0 * Math.PI
                val end = 3.0/4.0 * Math.PI
                (0..4).map { idx ->
                    val angle = (end - start) * idx / 4 + start
                    val xPx = centerPx + cos(angle) * radiusPx
                    val yPx = centerPx - sin(angle) * radiusPx
                    Pair(xPx.toFloat().toDp(), yPx.toFloat().toDp())
                }
            }
        }

        starOffsets.forEach { (x, y) ->
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier
                    .size(starSize)
                    .offset(x = x, y = y),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StickerbookScreenPreview() {
    StickerbookScreen()
}

// Bottom sheet composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReceiptScanBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
    sheetState: SheetState,
) {
    val options = Constants.storeTypes

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Simulate Receipt Scan",
                style = MaterialTheme.typography.titleMedium,
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = selectedOption ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select store type") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                            )
                        }
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .let { mod ->
                            if (showError && selectedOption == null) {
                                mod.border(1.dp, Color.Red, RoundedCornerShape(4.dp))
                            } else mod
                        },
                    isError = showError && selectedOption == null,
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                showError = false
                            },
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedOption == null) {
                        showError = true
                    } else {
                        onSubmit(selectedOption!!)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Submit")
            }
        }
    }
}
