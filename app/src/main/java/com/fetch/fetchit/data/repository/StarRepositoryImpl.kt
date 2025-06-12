package com.fetch.fetchit.data.repository

import com.fetch.fetchit.data.local.datasource.StarLocalDataSource
import com.fetch.fetchit.domain.repository.StarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StarRepositoryImpl @Inject constructor(
    private val localDataSource: StarLocalDataSource,
) : StarRepository {
    override fun getStars(storeType: String): Flow<Int> = localDataSource.getStars(storeType)

    override suspend fun incrementStars(storeType: String) = localDataSource.incrementStars(storeType)
}
