package com.fetch.fetchit.ui.stickerbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.fetchit.domain.usecase.GetStarsUseCase
import com.fetch.fetchit.domain.usecase.IncrementResult
import com.fetch.fetchit.domain.usecase.IncrementStarsUseCase
import com.fetch.fetchit.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoreStarsUiState(
    val groceryStars: Int = 0,
    val hardwareStars: Int = 0,
    val restaurantStars: Int = 0,
)

@HiltViewModel
class StickerbookViewModel @Inject constructor(
    getStarsUseCase: GetStarsUseCase,
    private val incrementStarsUseCase: IncrementStarsUseCase,
) : ViewModel() {

    private val _celebration = MutableSharedFlow<Pair<String, Int>>()
    val celebration = _celebration.asSharedFlow()

    val uiState: StateFlow<StoreStarsUiState> = combine(
        getStarsUseCase(Constants.grocery.name),
        getStarsUseCase(Constants.hardware.name),
        getStarsUseCase(Constants.restaurant.name),
    ) { grocery, hardware, restaurant ->
        StoreStarsUiState(
            groceryStars = grocery,
            hardwareStars = hardware,
            restaurantStars = restaurant,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StoreStarsUiState(),
    )

    fun onSubmit(storeType: String) {
        viewModelScope.launch {
            val result: IncrementResult = incrementStarsUseCase(storeType)
            if (result.celebrate) {
                _celebration.emit(storeType to result.newStars)
            }
        }
    }
}
