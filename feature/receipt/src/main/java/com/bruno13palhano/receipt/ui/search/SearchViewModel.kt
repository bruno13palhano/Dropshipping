package com.bruno13palhano.receipt.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.di.CacheRep
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.receipt.ui.shared.SearchAction
import com.bruno13palhano.receipt.ui.shared.SearchActionProcessor
import com.bruno13palhano.receipt.ui.shared.SearchEffect
import com.bruno13palhano.receipt.ui.shared.SearchEvent
import com.bruno13palhano.receipt.ui.shared.SearchState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @CacheRep private val cacheRepository: CacheRepository
): BaseViewModel<SearchState, SearchAction, SearchEvent, SearchEffect>(
    actionProcessor = SearchActionProcessor()
) {
    var query by mutableStateOf("")
        private set

    fun updateQuery(query: String) {
        this.query = query
    }

    fun onAction(action: SearchAction) {
        sendEvent(event = actionProcessor.processAction(action))
    }

    @Composable
    override fun states(events: Flow<SearchEvent>): SearchState {
        return searchPresenter(
            events = events,
            sendEffect = ::sendEffect,
            productRepository = productRepository,
            cacheRepository = cacheRepository
        )
    }
}