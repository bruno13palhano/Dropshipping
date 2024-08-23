package com.bruno13palhano.data.internal

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.data.internal.entity.CacheInternal

internal interface CacheDataSource : DataSource<CacheInternal> {
    suspend fun delete(query: String)
}