package com.bruno13palhano.data.repository

import com.bruno13palhano.data.di.CacheInternalDataSource
import com.bruno13palhano.data.internal.CacheDataSource
import com.bruno13palhano.data.internal.entity.asExternal
import com.bruno13palhano.data.internal.entity.asInternal
import com.bruno13palhano.model.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultCacheRepository @Inject constructor(
    @CacheInternalDataSource private val cacheDataSource: CacheDataSource
) : CacheRepository {
    override suspend fun insert(data: Cache) {
        cacheDataSource.insert(data = data.asInternal())
    }

    override suspend fun update(data: Cache) {
        cacheDataSource.update(data = data.asInternal())
    }

    override suspend fun delete(query: String) {
        cacheDataSource.delete(query = query)
    }

    override fun getAll(): Flow<List<Cache>> {
        return cacheDataSource.getAll().map {
            it.map { cacheInternal -> cacheInternal.asExternal() }
        }
    }
}