package com.bruno13palhano.data.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruno13palhano.data.internal.dao.CacheDao
import com.bruno13palhano.data.internal.dao.ProductDao
import com.bruno13palhano.data.internal.dao.ReceiptDao
import com.bruno13palhano.data.internal.entity.CacheInternal
import com.bruno13palhano.data.internal.entity.ProductInternal
import com.bruno13palhano.data.internal.entity.ReceiptInternal

@Database(
    entities = [
        ProductInternal::class,
        ReceiptInternal::class,
        CacheInternal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val receiptDao: ReceiptDao
    abstract val cacheDao: CacheDao
}