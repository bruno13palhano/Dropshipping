package com.bruno13palhano.data.di

import com.bruno13palhano.data.internal.CacheDataSource
import com.bruno13palhano.data.internal.ProductDataSource
import com.bruno13palhano.data.internal.ReceiptDataSource
import com.bruno13palhano.data.internal.dao.CacheDao
import com.bruno13palhano.data.internal.dao.ProductDao
import com.bruno13palhano.data.internal.dao.ReceiptDao
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.DefaultCacheRepository
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
internal annotation class CacheInternalDataSource

@Qualifier
annotation class ProductRep

@Qualifier
annotation class ReceiptRep

@Qualifier
annotation class CacheRep

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

    @CacheInternalDataSource
    @Singleton
    @Binds
    abstract fun bindCacheDataSource(
        cacheDataSource: CacheDao
    ): CacheDataSource


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

    @CacheRep
    @Singleton
    @Binds
    abstract fun bindCacheRepository(
        cacheRepository: DefaultCacheRepository
    ): CacheRepository
}