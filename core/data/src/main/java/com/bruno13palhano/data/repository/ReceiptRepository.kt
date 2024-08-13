package com.bruno13palhano.data.repository

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository : DataSource<Receipt> {
    fun getLastReceipts(limit: Int): Flow<List<Receipt>>
}