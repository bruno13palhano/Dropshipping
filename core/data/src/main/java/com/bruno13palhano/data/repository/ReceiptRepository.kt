package com.bruno13palhano.data.repository

import com.bruno13palhano.data.shared.DataSource
import com.bruno13palhano.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository : DataSource<Receipt> {
    suspend fun delete(id: Long)

    fun get(id: Long): Flow<Receipt>

    fun getLastReceipts(limit: Int): Flow<List<Receipt>>
}