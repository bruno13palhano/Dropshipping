package com.bruno13palhano.receipt.ui.viewmodel

import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.receipt.ui.shared.SearchEffect
import com.bruno13palhano.receipt.ui.shared.SearchEvent
import com.bruno13palhano.receipt.ui.shared.SearchReducer
import com.bruno13palhano.receipt.ui.shared.SearchState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): BaseViewModel<SearchState, SearchEvent, SearchEffect>(
    initialState = SearchState.INITIAL_STATE,
    reducer = SearchReducer()
) {
    fun onQueryChange(query: String) {
        sendEvent(event = SearchEvent.UpdateQuery(query = query))
    }

    fun onActiveChange(active: Boolean) {

    }

    fun onSearchClick(query: String) {

    }

    fun onCloseSearchClick() {

    }

    fun onDeleteSearchClick(query: String) {

    }

    fun navigateBack() {

    }

    fun navigateToAddReceipt(productId: Long) {

    }

    fun deleteCache(query: String) {

    }

    fun getCache() {

    }

    fun getProducts() {

    }

    fun searchProducts(query: String) {

    }
}