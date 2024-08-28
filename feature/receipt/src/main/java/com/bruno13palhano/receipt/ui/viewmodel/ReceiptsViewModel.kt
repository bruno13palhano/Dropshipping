package com.bruno13palhano.receipt.receiptsui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.CacheRep
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Cache
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.components.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    @ProductRep private val productRepository: ProductRepository,
    @CacheRep private val cacheRepository: CacheRepository
) : ViewModel() {
    private val _receipts = MutableStateFlow<List<CommonItem>>(emptyList())
    val receipts = _receipts
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _products = MutableStateFlow<List<CommonItem>>(emptyList())
    val products = _products
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val cache = cacheRepository.getAll()
        .map { it.map { cache -> cache.query } }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteCache(query: String) {
        viewModelScope.launch {
            cacheRepository.delete(query = query)
        }
    }

    fun deleteReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.delete(id = id)
        }
    }

    fun getReceipts() {
        viewModelScope.launch {
            receiptRepository.getAll().collect { receipts: List<Receipt> ->
                _receipts.update {
                    receipts.map { receipt ->
                        CommonItem(
                            id = receipt.id,
                            title = receipt.customerName
                        )
                    }
                }
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect { products: List<Product> ->
                _products.update {
                    products.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                }
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            cacheRepository.insert(data = Cache(query = query.trim()))
        }

        viewModelScope.launch {
            productRepository.search(query = query).collect { products: List<Product> ->
                _products.update {
                    products.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                }
            }
        }
    }
}