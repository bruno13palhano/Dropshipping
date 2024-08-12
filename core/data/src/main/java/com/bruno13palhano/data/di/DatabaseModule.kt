package com.bruno13palhano.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.internal.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase) = database.productDao

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
            )
            .fallbackToDestructiveMigrationFrom()
            .build()
    }
}