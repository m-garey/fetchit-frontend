package com.fetch.fetchit.domain.usecase

import com.fetch.fetchit.domain.repository.StarRepository
import javax.inject.Inject

class IncrementStarsUseCase @Inject constructor(
    private val repository: StarRepository,
) {
    suspend operator fun invoke(storeType: String) = repository.incrementStars(storeType)
}
