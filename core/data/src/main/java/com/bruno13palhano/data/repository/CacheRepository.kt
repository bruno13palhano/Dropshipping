package com.bruno13palhano.data.repository

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.model.Cache

interface CacheRepository : DataSource<Cache> {
    suspend fun delete(query: String)
}