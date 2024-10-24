package com.bruno13palhano.product.ui.products.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow

@Composable
internal fun productsPresenter(
    products: Flow<List<Product>>,
    events: Flow<ProductsEvent>,
    reducer: Reducer<ProductsState, ProductsEvent, ProductsEffect>,
    sendEvent: (event: ProductsEvent) -> Unit,
    sendEffect: (effect: ProductsEffect) -> Unit
): ProductsState {
    val state = remember { mutableStateOf(ProductsState.INITIAL_STATE) }

    HandleEvents(events = events, state = state, reducer = reducer, sendEffect = sendEffect)

    GetProducts(products = products, sendEvent = sendEvent)

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<ProductsEvent>,
    state: MutableState<ProductsState>,
    reducer: Reducer<ProductsState, ProductsEvent, ProductsEffect>,
    sendEffect: (effect: ProductsEffect) -> Unit
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(event = event, previousState = state.value).let {
                state.value = it.first
                it.second?.let(sendEffect)
            }
        }
    }
}

@Composable
private fun GetProducts(
    products: Flow<List<Product>>,
    sendEvent: (event: ProductsEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        products.collect {
            sendEvent(
                ProductsEvent.UpdateProducts(
                    products = it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                    }
                )
            )
        }
    }
}