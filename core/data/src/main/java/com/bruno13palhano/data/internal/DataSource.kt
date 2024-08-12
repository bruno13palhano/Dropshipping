package com.bruno13palhano.data.internal

import kotlinx.coroutines.flow.Flow

/**
 * Common interface for all data sources
 */
internal interface DataSource<T> {
    suspend fun insert(data: T)

    suspend fun update(data: T)

    suspend fun delete(id: Long)

    suspend fun get(id: Long): Flow<T>

    suspend fun getAll(): Flow<List<T>>
}