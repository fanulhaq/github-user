/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.di

import android.app.Application
import androidx.room.Room
import com.fanulhaq.githubuser.data.local.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DBModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application,
        RoomDB::class.java,
        "github_user"
    )
    .fallbackToDestructiveMigration()
    .build()

    @Singleton
    @Provides
    fun provideSearchDao(db: RoomDB) = db.searchDao()

    @Singleton
    @Provides
    fun provideDetailDao(db: RoomDB) = db.detailDao()

    @Singleton
    @Provides
    fun provideReposDao(db: RoomDB) = db.reposDao()

    @Singleton
    @Provides
    fun providePageKeyDao(db: RoomDB) = db.pageKeyDao()
}