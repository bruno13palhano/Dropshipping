package com.bruno13palhano.receipt.ui.receipt.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow

@Composable
internal fun newReceiptPresenter(
    receiptFields: ReceiptFields,
    productRepository: ProductRepository,
    receiptRepository: ReceiptRepository,
    reducer: Reducer<NewReceiptState, NewReceiptEvent, NewReceiptEffect>,
    events: Flow<NewReceiptEvent>,
    sendEvent: (event: NewReceiptEvent) -> Unit,
    sendEffect: (effect: NewReceiptEffect) -> Unit
): NewReceiptState {
    val state = remember { mutableStateOf(NewReceiptState.INITIAL_STATE) }

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
            insertReceipt(
                receiptRepository = receiptRepository,
                receiptFields = receiptFields,
                product = state.value.product
            )
        }
    }

    LaunchedEffect(state.value.product.id) {
        if (state.value.product.id == 0L) return@LaunchedEffect

        getProduct(
            productId = state.value.product.id,
            productRepository = productRepository,
            sendEvent = sendEvent
        )
    }

    return state.value
}

private suspend fun insertReceipt(
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    product: Product
) {
    receiptRepository.insert(data = receiptFields.toReceipt(product = product))
}

private suspend fun getProduct(
    productId: Long,
    productRepository: ProductRepository,
    sendEvent: (event: NewReceiptEvent) -> Unit
) {
    productRepository.get(id = productId).collect {
        sendEvent(NewReceiptEvent.UpdateProduct(product = it))
    }
}