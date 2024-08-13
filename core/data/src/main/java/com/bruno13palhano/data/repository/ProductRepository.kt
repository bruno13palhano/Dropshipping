package com.bruno13palhano.data.repository

import com.bruno13palhano.data.DataSource
import com.bruno13palhano.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository : DataSource<Product> {
    fun search(query: String): Flow<List<Product>>
}