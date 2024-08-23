package com.bruno13palhano.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.internal.database.AppDatabase
import com.bruno13palhano.data.internal.database.Converters
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
    fun provideReceiptDao(database: AppDatabase) = database.receiptDao

    @Provides
    @Singleton
    fun provideCacheDao(database: AppDatabase) = database.cacheDao

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, converters: Converters): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
            )
            .fallbackToDestructiveMigrationFrom()
            .addTypeConverter(converters)
            .build()
    }

    @Provides
    @Singleton
    fun provideTypeConverters() = Converters()
}