package com.bruno13palhano.receipt.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.CacheRep
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Cache
import com.bruno13palhano.receipt.ui.shared.ReceiptsEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptsEvent
import com.bruno13palhano.receipt.ui.shared.ReceiptsReducer
import com.bruno13palhano.receipt.ui.shared.ReceiptsState
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    @ProductRep private val productRepository: ProductRepository,
    @CacheRep private val cacheRepository: CacheRepository
) : BaseViewModel<ReceiptsState, ReceiptsEvent, ReceiptsEffect>(
    initialState = ReceiptsState.INITIAL_STATE,
    reducer = ReceiptsReducer()
) {
    fun onEditReceiptClick(id: Long) {
        sendEvent(event = ReceiptsEvent.EditReceipt(editReceipt = true, id = id))
    }

    fun onAddReceiptClick() {
        sendEvent(event = ReceiptsEvent.UpdateSearching(isSearching = true))
    }

    fun onSearchDoneClick(query: String) {
        sendEvent(event = ReceiptsEvent.OnSearchDoneClick(query = query))
    }

    fun onProductItemClick(id: Long) {
        sendEvent(event = ReceiptsEvent.OnProductItemClick(id = id))
    }

    fun onCloseSearchClick() {
        sendEvent(event = ReceiptsEvent.OnCloseSearchClick)
    }

    fun onDeleteCacheClick(query: String) {
        sendEvent(event = ReceiptsEvent.UpdateDeletingCache(isDeleting = true, query = query))
    }

    fun onDeleteReceiptClick(id: Long) {
        sendEvent(event = ReceiptsEvent.UpdateDeletingReceipt(isDeleting = true, id = id))
    }

    fun deleteCache(query: String) {
        viewModelScope.launch {
            cacheRepository.delete(query = query)
            sendEvent(event = ReceiptsEvent.OnDeleteCacheSuccessfully)
        }
    }

    fun deleteReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.delete(id = id)
            sendEvent(event = ReceiptsEvent.OnDeleteReceiptSuccessfully)
        }
    }

    fun getReceipts() {
        viewModelScope.launch {
            receiptRepository.getAll()
                .map {
                    it.map { receipt ->
                        CommonItem(
                            id = receipt.id,
                            title = receipt.customerName
                        )
                    }
                }
                .collect {
                    sendEvent(
                        event = ReceiptsEvent.UpdateReceipts(
                            isLoading = true,
                            receipts = it
                        )
                    )
                }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAll()
                .map {
                    it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                }
                .collect {
                    sendEvent(
                        event = ReceiptsEvent.UpdateProducts(
                            isLoading = true,
                            products = it
                        )
                    )
                }
        }
    }

    fun getCache() {
        viewModelScope.launch {
            cacheRepository.getAll()
                .map { it.map { cache -> cache.query } }
                .collect {
                    sendEvent(event = ReceiptsEvent.UpdateCache(isLoading = true, cache = it))
                }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            cacheRepository.insert(data = Cache(query = query.trim()))
        }

        viewModelScope.launch {
            productRepository.search(query = query)
                .map {
                    it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                }
                .collect {
                    sendEvent(
                        event = ReceiptsEvent.UpdateProducts(
                            isLoading = true,
                            products = it
                        )
                    )
                }
        }
    }
}