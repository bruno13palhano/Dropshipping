package com.bruno13palhano.receipt.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.CacheRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Cache
import com.bruno13palhano.ui.components.CommonItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
internal fun searchPresenter(
    cacheRepository: CacheRepository,
    productRepository: ProductRepository,
    events: Flow<SearchEvent>,
    sendEffect: (effect: SearchEffect) -> Unit
): SearchState {
    val state = remember { mutableStateOf(SearchState.INITIAL_STATE) }
    var deleting by remember { mutableStateOf(false) }
    var updatingCache by remember { mutableStateOf(false) }
    var updatingProducts by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    LaunchedEffect(active) {
        events.collect { event ->
            when (event) {
                SearchEvent.OnCloseSearchClick -> {
                    if (active) {
                        active = false
                        query = ""

                    } else {
                        sendEffect(SearchEffect.NavigateBack)
                    }
                }

                is SearchEvent.OnProductItemClick -> {
                    sendEffect(SearchEffect.NavigateToAddReceipt(productId = event.id))
                }

                is SearchEvent.OnSearchDoneClick -> {
                    query = event.query

                    if (event.query.isNotBlank()) {
                        updatingCache = true
                        updatingProducts = true
                    }
                }

                is SearchEvent.UpdateActive -> active = event.active

                is SearchEvent.UpdateDeleting -> {
                    deleting = event.deleting
                    query = event.query
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        getCache(previousState = state, cacheRepository = cacheRepository)
    }

    LaunchedEffect(query, updatingCache) {
        if (updatingCache) {
            active = false
            cacheRepository.insert(data = Cache(query = query.trim()))
            updatingCache = false
        }
    }

    LaunchedEffect(deleting) {
        if (deleting) {
            cacheRepository.delete(query = query)

            deleting = false
        }
    }

    LaunchedEffect(query) {
        getProducts(query = query, previousState = state, productRepository = productRepository)

        if (updatingProducts) updatingProducts = false
    }

    return SearchState(
        deleting = deleting,
        updatingCache = updatingCache,
        updatingProducts = updatingProducts,
        query = query,
        active = active,
        products = state.value.products,
        cache = state.value.cache
    )
}

private suspend fun getCache(
    previousState: MutableState<SearchState>,
    cacheRepository: CacheRepository
) {
    cacheRepository.getAll()
        .map { it.map { cache -> cache.query } }
        .collect {
            previousState.value = previousState.value.copy(cache = it)
        }
}

private suspend fun getProducts(
    query: String,
    previousState: MutableState<SearchState>,
    productRepository: ProductRepository
) {
    productRepository.search(query)
        .map { products ->
            products.map { product ->
                CommonItem(
                    id = product.id,
                    title = product.name
                )
            }
        }.collect {
            previousState.value = previousState.value.copy(products = it)
        }
}