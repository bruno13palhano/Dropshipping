package com.bruno13palhano.data.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.data.internal.dao.ProductDao
import com.bruno13palhano.data.internal.entity.ProductInternal

@Database(
    entities = [ProductInternal::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}