package com.fetch.fetchit.ui.stickerbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StickerbookRoute(
    viewModel: StickerbookViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val celebrationEvent by viewModel.celebration.collectAsState(initial = null)

    StickerbookScreen(
        state = state,
        onSubmit = viewModel::onSubmit,
        celebrationEvent = celebrationEvent,
    )
}
