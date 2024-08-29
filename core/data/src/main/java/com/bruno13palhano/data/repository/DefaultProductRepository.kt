package com.bruno13palhano.data.repository

import com.bruno13palhano.data.di.ProductInternalDataSource
import com.bruno13palhano.data.internal.datasource.ProductDataSource
import com.bruno13palhano.data.internal.entity.asExternal
import com.bruno13palhano.data.internal.entity.asInternal
import com.bruno13palhano.model.Product
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultProductRepository @Inject constructor(
    @ProductInternalDataSource private val productDataSource: ProductDataSource
): ProductRepository {
    override fun search(query: String): Flow<List<Product>> {
        return productDataSource.search(query = query).map {
            it.map { productInternal -> productInternal.asExternal() }
        }
    }

    override suspend fun insert(data: Product) {
        productDataSource.insert(data = data.asInternal())
    }

    override suspend fun update(data: Product) {
        productDataSource.update(data = data.asInternal())
    }

    override suspend fun delete(id: Long) {
        productDataSource.delete(id = id)
    }

    override fun get(id: Long): Flow<Product> {
        return productDataSource.get(id = id).map { it.asExternal() }
    }

    override fun getAll(): Flow<List<Product>> {
        return productDataSource.getAll().map {
            it.map { productInternal -> productInternal.asExternal() }
        }
    }
}