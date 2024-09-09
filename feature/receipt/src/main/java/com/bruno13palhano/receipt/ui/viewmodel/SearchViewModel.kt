package com.bruno13palhano.receipt.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.CacheRep
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Cache
import com.bruno13palhano.receipt.ui.shared.SearchEffect
import com.bruno13palhano.receipt.ui.shared.SearchEvent
import com.bruno13palhano.receipt.ui.shared.SearchReducer
import com.bruno13palhano.receipt.ui.shared.SearchState
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @CacheRep private val cacheRepository: CacheRepository
): BaseViewModel<SearchState, SearchEvent, SearchEffect>(
    initialState = SearchState.INITIAL_STATE,
    reducer = SearchReducer()
) {
    fun onQueryChange(query: String) {
        sendEvent(event = SearchEvent.UpdateQuery(query = query))
    }

    fun onActiveChange(active: Boolean) {
        sendEvent(event = SearchEvent.UpdateActive(active = active))
    }

    fun onSearchClick(query: String) {
        sendEvent(event = SearchEvent.OnSearchDoneClick(query = query))
    }

    fun onCloseSearchClick() {
        sendEvent(event = SearchEvent.OnCloseSearchClick)
    }

    fun onDeleteSearchClick(query: String) {
        sendEvent(event = SearchEvent.UpdateDeleting(deleting = true, query = query))
    }

    fun navigateToAddReceipt(productId: Long) {
        sendEvent(event = SearchEvent.OnProductItemClick(id = productId))
    }

    fun deleteCache(query: String) {
        viewModelScope.launch {
            cacheRepository.delete(query = query)
            sendEvent(event = SearchEvent.UpdateDeleting(deleting = false, query = ""))
        }
    }

    fun getCache() {
        viewModelScope.launch {
            cacheRepository.getAll()
                .map { it.map { cache -> cache.query } }
                .collect { sendEvent(event = SearchEvent.UpdateCache(cache = it)) }
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
                .collect { sendEvent(event = SearchEvent.UpdateProducts(products = it)) }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch { cacheRepository.insert(data = Cache(query = query.trim())) }

        viewModelScope.launch {
            productRepository.search(query = query.trim())
                .map {
                    it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                }
                .collect { sendEvent(event = SearchEvent.UpdateProducts(products = it)) }
        }
    }
}