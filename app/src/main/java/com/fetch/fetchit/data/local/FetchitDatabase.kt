package com.fetch.fetchit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fetch.fetchit.data.local.dao.StoreStarsDao
import com.fetch.fetchit.data.local.entities.StoreStarsEntity

@Database(
    entities = [StoreStarsEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class FetchitDatabase : RoomDatabase() {

    abstract fun storeStarsDao(): StoreStarsDao

    companion object {
        private const val DATABASE_NAME = "fetchit-db"

        fun build(context: Context): FetchitDatabase = Room.databaseBuilder(
            context,
            FetchitDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
