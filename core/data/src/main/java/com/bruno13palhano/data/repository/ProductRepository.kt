package com.bruno13palhano.data.repository

import com.bruno13palhano.data.shared.DataSource
import com.bruno13palhano.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository : DataSource<Product> {
    suspend fun delete(id: Long)

    fun get(id: Long): Flow<Product>

    fun search(query: String): Flow<List<Product>>
}