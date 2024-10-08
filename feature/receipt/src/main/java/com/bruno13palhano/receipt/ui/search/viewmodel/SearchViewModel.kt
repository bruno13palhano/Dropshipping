package com.bruno13palhano.receipt.ui.search.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.di.CacheRep
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.receipt.ui.search.presenter.SearchAction
import com.bruno13palhano.receipt.ui.search.presenter.SearchActionProcessor
import com.bruno13palhano.receipt.ui.search.presenter.SearchEffect
import com.bruno13palhano.receipt.ui.search.presenter.SearchEvent
import com.bruno13palhano.receipt.ui.search.presenter.SearchReducer
import com.bruno13palhano.receipt.ui.search.presenter.SearchState
import com.bruno13palhano.receipt.ui.search.presenter.searchPresenter
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @CacheRep private val cacheRepository: CacheRepository
): BaseViewModel<SearchState, SearchAction, SearchEvent, SearchEffect>(
    actionProcessor = SearchActionProcessor(),
    reducer = SearchReducer()
) {
    var query by mutableStateOf("")
        private set

    fun updateQuery(query: String) {
        this.query = query
    }

    @Composable
    override fun states(events: Flow<SearchEvent>): SearchState {
        return searchPresenter(
            events = events,
            sendEvent = ::sendEvent,
            sendEffect = ::sendEffect,
            reducer = reducer,
            productRepository = productRepository,
            cacheRepository = cacheRepository
        )
    }
}