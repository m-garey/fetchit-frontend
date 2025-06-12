package com.fetch.fetchit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a star count tracked per store type.
 * [storeType] should map directly to the constants defined in [com.fetch.fetchit.utils.Constants].
 */
@Entity(tableName = "store_stars")
data class StoreStarsEntity(
    @PrimaryKey val storeType: String,
    val stars: Int,
)
