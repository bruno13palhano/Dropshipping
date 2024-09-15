package com.bruno13palhano.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bruno13palhano.data.internal.datasource.ReceiptDataSource
import com.bruno13palhano.data.internal.entity.asExternal
import com.bruno13palhano.model.Receipt

internal class ReceiptsPagingSource(
    private val receiptDataSource: ReceiptDataSource,
    private var offset: Int,
    private val limit: Int
) : PagingSource<Int, Receipt>() {
    override fun getRefreshKey(state: PagingState<Int, Receipt>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Receipt> {
        return try {
            val currentPage = params.key ?: 1
            val receipts = receiptDataSource.pagingReceipts(offset = offset, limit = limit)

            offset += limit

            LoadResult.Page(
                data = receipts.map { it.asExternal() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (receipts.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}