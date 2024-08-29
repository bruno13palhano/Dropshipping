package com.bruno13palhano.data.internal.datasource

import com.bruno13palhano.data.shared.DataSource
import com.bruno13palhano.data.internal.entity.CacheInternal

internal interface CacheDataSource : DataSource<CacheInternal> {
    suspend fun delete(query: String)
}