package com.bruno13palhano.data.repository

import com.bruno13palhano.data.di.ReceiptInternalDataSource
import com.bruno13palhano.data.internal.datasource.ReceiptDataSource
import com.bruno13palhano.data.internal.entity.asExternal
import com.bruno13palhano.data.internal.entity.asInternal
import com.bruno13palhano.model.Receipt
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultReceiptRepository @Inject constructor(
    @ReceiptInternalDataSource private val receiptDataSource: ReceiptDataSource
): ReceiptRepository {
    override fun getLastReceipts(limit: Int): Flow<List<Receipt>> {
        return receiptDataSource.getLastReceipts(limit = limit).map {
            it.map { receiptInternal -> receiptInternal.asExternal() }
        }
    }

    override suspend fun insert(data: Receipt) {
        receiptDataSource.insert(data = data.asInternal())
    }

    override suspend fun update(data: Receipt) {
        receiptDataSource.update(data = data.asInternal())
    }

    override suspend fun delete(id: Long) {
        receiptDataSource.delete(id = id)
    }

    override fun get(id: Long): Flow<Receipt> {
        return receiptDataSource.get(id = id).map { it.asExternal() }
    }

    override fun getAll(): Flow<List<Receipt>> {
        return receiptDataSource.getAll().map {
            it.map { receiptInternal -> receiptInternal.asExternal() }
        }
    }
}