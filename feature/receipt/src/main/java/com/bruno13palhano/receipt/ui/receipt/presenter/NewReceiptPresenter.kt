package com.bruno13palhano.receipt.ui.receipt.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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

    HandleEvents(
        events = events,
        state = state,
        reducer = reducer,
        sendEffect = sendEffect
    )

    InsertNewReceipt(
        insert = state.value.insert,
        receiptRepository = receiptRepository,
        receiptFields = receiptFields,
        product = state.value.product
    )

    GetProduct(
        productId = state.value.product.id,
        productRepository = productRepository,
        sendEvent = sendEvent
    )

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<NewReceiptEvent>,
    state: MutableState<NewReceiptState>,
    reducer: Reducer<NewReceiptState, NewReceiptEvent, NewReceiptEffect>,
    sendEffect: (effect: NewReceiptEffect) -> Unit
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
private fun InsertNewReceipt(
    insert: Boolean,
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    product: Product
) {
    LaunchedEffect(insert) {
        if (insert) {
            receiptRepository.insert(data = receiptFields.toReceipt(product = product))
        }
    }
}

@Composable
private fun GetProduct(
    productId: Long,
    productRepository: ProductRepository,
    sendEvent: (event: NewReceiptEvent) -> Unit
) {
    LaunchedEffect(productId) {
        if (productId == 0L) return@LaunchedEffect

        productRepository.get(id = productId).collect {
            sendEvent(NewReceiptEvent.UpdateProduct(product = it))
        }
    }
}