package com.bruno13palhano.receipt.ui.search.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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

    HandleEvents(events = events, state = state, reducer = reducer, sendEffect = sendEffect)

    InsertSearch(
        insert = state.value.insert,
        query = state.value.query,
        cacheRepository = cacheRepository
    )

    DeleteSearch(
        delete = state.value.delete,
        query = state.value.query,
        cacheRepository = cacheRepository
    )

    GetCache(cacheRepository = cacheRepository, sendEvent = sendEvent)

    GetProducts(
        query = state.value.query,
        productRepository = productRepository,
        sendEvent = sendEvent
    )

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<SearchEvent>,
    state: MutableState<SearchState>,
    reducer: Reducer<SearchState, SearchEvent, SearchEffect>,
    sendEffect: (effect: SearchEffect) -> Unit
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(previousState = state.value, event = event).let {
                state.value = it.first
                it.second?.let { effect -> sendEffect(effect) }
            }
        }
    }
}

@Composable
private fun InsertSearch(
    insert: Boolean,
    query: String,
    cacheRepository: CacheRepository,
) {
    LaunchedEffect(insert) { cacheRepository.insert(Cache(query = query.trim())) }
}

@Composable
private fun DeleteSearch(
    delete: Boolean,
    query: String,
    cacheRepository: CacheRepository,
) {
    LaunchedEffect(delete) { cacheRepository.delete(query = query) }
}

@Composable
private fun GetCache(
    cacheRepository: CacheRepository,
    sendEvent: (event: SearchEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        cacheRepository.getAll()
            .map { it.map { cache -> cache.query } }
            .collect { sendEvent(SearchEvent.UpdateCache(cache = it)) }
    }
}

@Composable
private fun GetProducts(
    query: String,
    productRepository: ProductRepository,
    sendEvent: (event: SearchEvent) -> Unit
) {
    LaunchedEffect(query) {
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
}