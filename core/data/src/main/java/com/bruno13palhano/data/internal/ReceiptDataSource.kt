package com.bruno13palhano.data.internal

import com.bruno13palhano.data.internal.entity.ReceiptInternal
import kotlinx.coroutines.flow.Flow

internal interface ReceiptDataSource : DataSource<ReceiptInternal> {
    fun getLastReceipts(limit: Int): Flow<List<ReceiptInternal>>
}