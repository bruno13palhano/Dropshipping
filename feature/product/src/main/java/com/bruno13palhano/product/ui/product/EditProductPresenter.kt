package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(previousState = state.value, event = event).let {
                state.value = it.first
                it.second?.let { effect -> sendEffect(effect) }
            }
        }
    }

    LaunchedEffect(state.value.update) {
        if (state.value.update) {
            updateProduct(
                id = state.value.id,
                productFields = productFields,
                productRepository = productRepository
            )
        }
    }

    LaunchedEffect(state.value.delete) {
        if (state.value.delete) {
            deleteProduct(
                id = state.value.id,
                productRepository = productRepository
            )
        }
    }

    LaunchedEffect(state.value.id) {
        if (state.value.id == 0L) return@LaunchedEffect

        getProduct(
            id = state.value.id,
            productFields = productFields,
            productRepository = productRepository
        )
    }

    return state.value
}

private suspend fun updateProduct(
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.update(data = productFields.toProduct(id = id))
}

private suspend fun deleteProduct(
    id: Long,
    productRepository: ProductRepository
) {
    productRepository.delete(id = id)
}

private suspend fun getProduct(
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.get(id = id)
        .catch { it.printStackTrace() }
        .collect {
            productFields.setFields(product = it)
        }
}