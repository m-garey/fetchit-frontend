package com.fetch.fetchit.domain.usecase

import com.fetch.fetchit.domain.repository.StarRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class IncrementResult(
    val newStars: Int,
    val celebrate: Boolean,
)

class IncrementStarsUseCase @Inject constructor(
    private val repository: StarRepository,
) {

    /**
     * Increments stars for the given category and returns an [IncrementResult].
     * The `celebrate` flag is true when the new star count is a positive multiple of 5.
     */
    suspend operator fun invoke(storeType: String): IncrementResult {
        repository.incrementStars(storeType)
        val current = repository.getStars(storeType).first()
        val celebrate = current > 0 && current % 5 == 0
        return IncrementResult(newStars = current, celebrate = celebrate)
    }
}
