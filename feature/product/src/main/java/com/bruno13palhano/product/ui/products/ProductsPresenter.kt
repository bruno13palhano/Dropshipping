package com.bruno13palhano.product.ui.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(event = event, previousState = state.value).let {
                state.value = it.first
                it.second?.let(sendEffect)
            }
        }
    }

    LaunchedEffect(Unit) {
        getProducts(products = products, sendEvent = sendEvent)
    }

    return state.value
}

private suspend fun getProducts(
    products: Flow<List<Product>>,
    sendEvent: (event: ProductsEvent) -> Unit
) {
    products.collect {
        sendEvent(
            ProductsEvent.UpdateProducts(
                products =
                    it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name
                        )
                }
            )
        )
    }
}