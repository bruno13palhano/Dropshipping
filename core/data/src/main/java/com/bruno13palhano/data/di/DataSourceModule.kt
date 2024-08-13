package com.bruno13palhano.data.di

import com.bruno13palhano.data.internal.ProductDataSource
import com.bruno13palhano.data.internal.ReceiptDataSource
import com.bruno13palhano.data.internal.dao.ProductDao
import com.bruno13palhano.data.internal.dao.ReceiptDao
import com.bruno13palhano.data.repository.DefaultProductRepository
import com.bruno13palhano.data.repository.DefaultReceiptRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class ProductInternalDataSource

@Qualifier
internal annotation class ReceiptInternalDataSource

@Qualifier
annotation class ProductRep

@Qualifier
annotation class ReceiptRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataSourceModule {
    @ProductInternalDataSource
    @Singleton
    @Binds
    abstract fun bindProductDataSource(
        productDataSource: ProductDao
    ): ProductDataSource

    @ReceiptInternalDataSource
    @Singleton
    @Binds
    abstract fun bindReceiptDataSource(
        receiptDataSource: ReceiptDao
    ): ReceiptDataSource

    @ProductRep
    @Singleton
    @Binds
    abstract fun bindProductRepository(
        productRepository: DefaultProductRepository
    ): ProductRepository

    @ReceiptRep
    @Singleton
    @Binds
    abstract fun bindReceiptRepository(
        receiptRepository: DefaultReceiptRepository
    ): ReceiptRepository
}