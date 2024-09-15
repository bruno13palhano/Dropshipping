package com.bruno13palhano.data.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.internal.datasource.ReceiptDataSource
import com.bruno13palhano.data.internal.entity.ReceiptInternal
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ReceiptDao : ReceiptDataSource {
    @Insert
    override suspend fun insert(data: ReceiptInternal)

    @Update
    override suspend fun update(data: ReceiptInternal)

    @Query("DELETE FROM ReceiptInternal WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM ReceiptInternal WHERE id = :id")
    override fun get(id: Long): Flow<ReceiptInternal>

    @Query("SELECT * FROM ReceiptInternal ORDER BY id DESC")
    override fun getAll(): Flow<List<ReceiptInternal>>

    @Query("SELECT * FROM ReceiptInternal ORDER BY id DESC LIMIT :limit")
    override fun getLastReceipts(limit: Int): Flow<List<ReceiptInternal>>

    @Query("SELECT * FROM ReceiptInternal ORDER BY id DESC LIMIT :limit OFFSET :offset")
    override suspend fun pagingReceipts(offset: Int, limit: Int): List<ReceiptInternal>
}