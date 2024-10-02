package com.bruno13palhano.receipt.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Cache
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
internal fun searchPresenter(
    cacheRepository: CacheRepository,
    productRepository: ProductRepository,
    reducer: Reducer<SearchState, SearchEvent, SearchEffect>,
    events: Flow<SearchEvent>,
    sendEvent: (event: SearchEvent) -> Unit,
    sendEffect: (effect: SearchEffect) -> Unit
): SearchState {
    val state = remember { mutableStateOf(SearchState.INITIAL_STATE) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(event = event, previousState = state.value).let {
                state.value = it.first
                it.second?.let { effect -> sendEffect(effect) }
            }
        }
    }

    LaunchedEffect(state.value.insert) {
        if (state.value.insert) {
            cacheRepository.insert(Cache(query = state.value.query.trim()))
        }
    }

    LaunchedEffect(state.value.delete) {
        if (state.value.delete) {
            cacheRepository.delete(query = state.value.query)
        }
    }

    LaunchedEffect(Unit) {
        getCache(cacheRepository = cacheRepository, sendEvent = sendEvent)
    }

    LaunchedEffect(state.value.query) {
        getProducts(
            query = state.value.query,
            productRepository = productRepository,
            sendEvent = sendEvent
        )
    }

    return state.value
}

private suspend fun getCache(
    cacheRepository: CacheRepository,
    sendEvent: (event: SearchEvent) -> Unit,
) {
    cacheRepository.getAll()
        .map { it.map { cache -> cache.query } }
        .collect { sendEvent(SearchEvent.UpdateCache(cache = it)) }
}

private suspend fun getProducts(
    query: String,
    productRepository: ProductRepository,
    sendEvent: (event: SearchEvent) -> Unit
) {
    productRepository.search(query)
        .map { products ->
            products.map { product ->
                CommonItem(
                    id = product.id,
                    title = product.name
                )
            }
        }
        .collect { sendEvent(SearchEvent.UpdateProducts(products = it)) }
}