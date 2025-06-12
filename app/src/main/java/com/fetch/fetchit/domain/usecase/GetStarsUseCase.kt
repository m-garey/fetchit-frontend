package com.fetch.fetchit.domain.usecase

import com.fetch.fetchit.domain.repository.StarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStarsUseCase @Inject constructor(
    private val repository: StarRepository,
) {
    operator fun invoke(storeType: String): Flow<Int> = repository.getStars(storeType)
}
