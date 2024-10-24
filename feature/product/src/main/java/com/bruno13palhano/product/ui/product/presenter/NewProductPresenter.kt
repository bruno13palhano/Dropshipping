package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow

@Composable
internal fun newProductPresenter(
    productFields: ProductFields,
    productRepository: ProductRepository,
    reducer: Reducer<NewProductState, NewProductEvent, NewProductEffect>,
    events: Flow<NewProductEvent>,
    sendEffect: (effect: NewProductEffect) -> Unit
): NewProductState {
    val state = remember { mutableStateOf(NewProductState.INITIAL_STATE) }

    HandleEvents(events = events, state = state, reducer = reducer, sendEffect = sendEffect)

    InsertNewProduct(
        insert = state.value.insert,
        productFields = productFields,
        productRepository = productRepository
    )

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<NewProductEvent>,
    state: MutableState<NewProductState>,
    reducer: Reducer<NewProductState, NewProductEvent, NewProductEffect>,
    sendEffect: (effect: NewProductEffect) -> Unit
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
private fun InsertNewProduct(
    insert: Boolean,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    LaunchedEffect(insert) {
        if (insert) {
            productRepository.insert(data = productFields.toProduct())
        }
    }
}