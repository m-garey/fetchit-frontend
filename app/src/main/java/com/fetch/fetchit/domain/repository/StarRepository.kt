package com.fetch.fetchit.domain.repository

import kotlinx.coroutines.flow.Flow

interface StarRepository {
    fun getStars(storeType: String): Flow<Int>
    suspend fun incrementStars(storeType: String)
}
