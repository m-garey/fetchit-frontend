package com.fetch.fetchit.data.local.datasource

import com.fetch.fetchit.utils.Constants
import kotlinx.coroutines.flow.Flow

interface StarLocalDataSource {

    fun getStars(storeType: String): Flow<Int>

    suspend fun incrementStars(storeType: String)

    companion object {
        val ALL_STORE_TYPES = listOf(
            Constants.grocery.name,
            Constants.hardware.name,
            Constants.restaurant.name,
        )
    }
}
