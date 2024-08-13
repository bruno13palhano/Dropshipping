package com.bruno13palhano.data.internal

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.data.internal.entity.ProductInternal
import kotlinx.coroutines.flow.Flow

internal interface ProductDataSource : DataSource<ProductInternal> {
    fun search(query: String): Flow<List<ProductInternal>>
}