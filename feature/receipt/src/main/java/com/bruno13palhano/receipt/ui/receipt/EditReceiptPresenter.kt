package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(previousState = state.value, event = event).let {
                state.value = it.first
                it.second?.let { effect -> sendEffect(effect) }
            }
        }
    }

    LaunchedEffect(state.value.edit) {
        if (state.value.edit) {
            updateReceipt(
                receiptId = state.value.receipt.id,
                receiptRepository = receiptRepository,
                receiptFields = receiptFields,
                product = state.value.receipt.product
            )
        }
    }

    LaunchedEffect(state.value.cancel) {
        if (state.value.cancel) {
            cancelReceipt(
                receipt = state.value.receipt,
                receiptRepository = receiptRepository
            )
        }
    }

    LaunchedEffect(state.value.delete) {
        if (state.value.delete) {
            deleteReceipt(
                receiptId = state.value.receipt.id,
                receiptRepository = receiptRepository
            )
        }
    }

    LaunchedEffect(state.value.receipt.id) {
        if (state.value.receipt.id == 0L) return@LaunchedEffect

        getReceipt(
            receiptId = state.value.receipt.id,
            receiptRepository = receiptRepository,
            sendEvent = sendEvent
        )
    }

    return state.value
}

private suspend fun updateReceipt(
    receiptId: Long,
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    product: Product
) {
    receiptRepository.update(
        data = receiptFields.toReceipt(
            id = receiptId,
            product = product
        )
    )
}

private suspend fun deleteReceipt(
    receiptId: Long,
    receiptRepository: ReceiptRepository
) {
    receiptRepository.delete(id = receiptId)
}

private suspend fun cancelReceipt(
    receipt: Receipt,
    receiptRepository: ReceiptRepository
) {
    receiptRepository.update(data = receipt.copy(canceled = true))
}

private suspend fun getReceipt(
    receiptId: Long,
    receiptRepository: ReceiptRepository,
    sendEvent: (event: EditReceiptEvent) -> Unit
) {
    receiptRepository.get(id = receiptId)
        .catch { it.printStackTrace() }
        .collect {
            sendEvent(EditReceiptEvent.UpdateReceipt(receipt = it))
        }
}