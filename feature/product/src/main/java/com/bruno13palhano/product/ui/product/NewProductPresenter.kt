package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(event = event, previousState = state.value).let {
                state.value = it.first
                it.second?.let(sendEffect)
            }
        }
    }

    LaunchedEffect(state.value.insert) {
        if (state.value.insert) {
            insertProduct(productFields = productFields, productRepository = productRepository)
        }
    }

    return state.value
}

private suspend fun insertProduct(
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.insert(data = productFields.toProduct())
}