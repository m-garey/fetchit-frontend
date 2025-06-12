package com.fetch.fetchit.di

import android.content.Context
import com.fetch.fetchit.data.local.FetchitDatabase
import com.fetch.fetchit.data.local.dao.StoreStarsDao
import com.fetch.fetchit.data.local.datasource.RoomStarLocalDataSource
import com.fetch.fetchit.data.local.datasource.StarLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    @Binds
    fun bindsStarLocalDataSource(
        impl: RoomStarLocalDataSource,
    ): StarLocalDataSource

    @Binds
    fun bindsStarRepository(
        impl: com.fetch.fetchit.data.repository.StarRepositoryImpl,
    ): com.fetch.fetchit.domain.repository.StarRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context,
        ): FetchitDatabase = FetchitDatabase.build(context)

        @Provides
        fun provideStoreStarsDao(
            db: FetchitDatabase,
        ): StoreStarsDao = db.storeStarsDao()
    }
}
