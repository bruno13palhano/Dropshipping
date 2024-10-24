package com.bruno13palhano.receipt.ui.receipt.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Composable
internal fun editReceiptPresenter(
    receiptFields: ReceiptFields,
    receiptRepository: ReceiptRepository,
    reducer: Reducer<EditReceiptState, EditReceiptEvent, EditReceiptEffect>,
    events: Flow<EditReceiptEvent>,
    sendEvent: (event: EditReceiptEvent) -> Unit,
    sendEffect: (effect: EditReceiptEffect) -> Unit
): EditReceiptState {
    val state = remember { mutableStateOf(EditReceiptState.INITIAL_STATE) }

    HandleEvents(
        events = events,
        state = state,
        reducer = reducer,
        sendEffect = sendEffect
    )

    UpdateReceipt(
        edit = state.value.edit,
        receiptId = state.value.receipt.id,
        receiptRepository = receiptRepository,
        receiptFields = receiptFields,
        product = state.value.receipt.product
    )

    CancelReceipt(
        cancel = state.value.cancel,
        receipt = state.value.receipt,
        receiptRepository = receiptRepository
    )

    DeleteReceipt(
        delete = state.value.delete,
        receiptId = state.value.receipt.id,
        receiptRepository = receiptRepository
    )

    SetCurrentReceiptState(
        receiptId = state.value.receipt.id,
        receiptRepository = receiptRepository,
        sendEvent = sendEvent
    )

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<EditReceiptEvent>,
    state: MutableState<EditReceiptState>,
    reducer: Reducer<EditReceiptState, EditReceiptEvent, EditReceiptEffect>,
    sendEffect: (effect: EditReceiptEffect) -> Unit
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
private fun UpdateReceipt(
    edit: Boolean,
    receiptId: Long,
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    product: Product
) {
    LaunchedEffect(edit) {
        if (edit) {
            receiptRepository.update(
                data = receiptFields.toReceipt(
                    id = receiptId,
                    product = product
                )
            )
        }
    }
}

@Composable
private fun CancelReceipt(
    cancel: Boolean,
    receipt: Receipt,
    receiptRepository: ReceiptRepository
) {
    LaunchedEffect(cancel) {
        if (cancel) {
            receiptRepository.update(data = receipt.copy(canceled = true))
        }
    }
}

@Composable
private fun DeleteReceipt(
    delete: Boolean,
    receiptId: Long,
    receiptRepository: ReceiptRepository
) {
    LaunchedEffect(delete) {
        if (delete) {
            receiptRepository.delete(id = receiptId)
        }
    }
}

@Composable
private fun SetCurrentReceiptState(
    receiptId: Long,
    receiptRepository: ReceiptRepository,
    sendEvent: (event: EditReceiptEvent) -> Unit
) {
    LaunchedEffect(receiptId) {
        if (receiptId == 0L) return@LaunchedEffect

        receiptRepository.get(id = receiptId)
            .catch { it.printStackTrace() }
            .collect {
                sendEvent(EditReceiptEvent.UpdateReceipt(receipt = it))
            }
    }
}