package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Composable
internal fun editProductPresenter(
    productFields: ProductFields,
    productRepository: ProductRepository,
    reducer: Reducer<EditProductState, EditProductEvent, EditProductEffect>,
    events: Flow<EditProductEvent>,
    sendEffect: (effect: EditProductEffect) -> Unit
): EditProductState {
    val state = remember { mutableStateOf(EditProductState.INITIAL_STATE) }

    HandleEvents(
        events = events,
        state = state,
        reducer = reducer,
        sendEffect = sendEffect
    )

    UpdateProduct(
        update = state.value.update,
        id = state.value.id,
        productFields = productFields,
        productRepository = productRepository
    )

    DeleteProduct(
        delete = state.value.delete,
        id = state.value.id,
        productRepository = productRepository
    )

    SetCurrentProductState(
        id = state.value.id,
        productFields = productFields,
        productRepository = productRepository
    )

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<EditProductEvent>,
    state: MutableState<EditProductState>,
    reducer: Reducer<EditProductState, EditProductEvent, EditProductEffect>,
    sendEffect: (effect: EditProductEffect) -> Unit
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
private fun UpdateProduct(
    update: Boolean,
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    LaunchedEffect(update) {
        if (update) productRepository.update(data = productFields.toProduct(id = id))
    }
}

@Composable
private fun DeleteProduct(
    delete: Boolean,
    id: Long,
    productRepository: ProductRepository
) {
    LaunchedEffect(delete) {
        if (delete) productRepository.delete(id = id)
    }
}

@Composable
private fun SetCurrentProductState(
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    LaunchedEffect(id) {
        if (id == 0L) return@LaunchedEffect

        productRepository.get(id = id)
            .catch { it.printStackTrace() }
            .collect { productFields.setFields(product = it) }
    }
}