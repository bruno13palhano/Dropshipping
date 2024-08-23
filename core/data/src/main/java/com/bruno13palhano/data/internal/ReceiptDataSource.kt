package com.bruno13palhano.data.internal

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.data.internal.entity.ReceiptInternal
import kotlinx.coroutines.flow.Flow

internal interface ReceiptDataSource : DataSource<ReceiptInternal> {
    suspend fun delete(id: Long)
    fun get(id: Long): Flow<ReceiptInternal>
    fun getLastReceipts(limit: Int): Flow<List<ReceiptInternal>>
}