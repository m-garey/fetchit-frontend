package com.fetch.fetchit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fetch.fetchit.data.local.entities.StoreStarsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreStarsDao {

    @Query("SELECT stars FROM store_stars WHERE storeType = :storeType")
    fun getStars(storeType: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: StoreStarsEntity)

    @Update
    suspend fun update(entity: StoreStarsEntity)

    /**
     * Increments the star count for the given store type, inserting a new row if necessary.
     */
    @Query("UPDATE store_stars SET stars = stars + 1 WHERE storeType = :storeType")
    suspend fun increment(storeType: String): Int
}
