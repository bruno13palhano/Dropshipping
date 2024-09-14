package com.bruno13palhano.domain

import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Singleton
    @Provides
    fun provideHomeUseCase(@ReceiptRep receiptRepository: ReceiptRepository): HomeUseCase {
        return HomeUseCase(receiptRepository = receiptRepository)
    }
}