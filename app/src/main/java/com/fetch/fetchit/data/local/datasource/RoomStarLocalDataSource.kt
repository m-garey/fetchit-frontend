package com.fetch.fetchit.data.local.datasource

import com.fetch.fetchit.data.local.dao.StoreStarsDao
import com.fetch.fetchit.data.local.entities.StoreStarsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Room-backed implementation of [StarLocalDataSource]. Handles first-time insertion gracefully.
 */
class RoomStarLocalDataSource @Inject constructor(
    private val dao: StoreStarsDao,
) : StarLocalDataSource {

    override fun getStars(storeType: String): Flow<Int> = dao.getStars(storeType).map { it ?: 0 }

    override suspend fun incrementStars(storeType: String) {
        val updatedRows = dao.increment(storeType)
        if (updatedRows == 0) {
            // row doesn't exist yet, insert baseline then increment again
            dao.insert(StoreStarsEntity(storeType = storeType, stars = 0))
            dao.increment(storeType)
        }
    }
}
