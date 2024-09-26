package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Composable
internal fun editReceiptPresenter(
    receiptFields: ReceiptFields,
    receiptRepository: ReceiptRepository,
    events: Flow<EditReceiptEvent>,
    sendEffect: (effect: EditReceiptEffect) -> Unit
): EditReceiptState {
    val state = remember { mutableStateOf(EditReceiptState.INITIAL_STATE) }
    var hasInvalidField by remember { mutableStateOf(false) }
    var currentReceiptId by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EditReceiptEvent.Cancel -> {
                    cancelReceipt(
                        receiptId = currentReceiptId,
                        previousState = state,
                        receiptRepository = receiptRepository
                    )

                    sendEffect(EditReceiptEffect.NavigateBack)
                }

                is EditReceiptEvent.SetInitialData -> {
                    currentReceiptId = event.id
                }

                is EditReceiptEvent.Delete -> {
                    deleteReceipt(
                        receiptId = currentReceiptId,
                        receiptRepository = receiptRepository
                    )

                    sendEffect(EditReceiptEffect.NavigateBack)
                }

                is EditReceiptEvent.Done -> {
                    if (!receiptFields.isReceiptValid()) {
                        hasInvalidField = true
                    } else {
                        hasInvalidField = false

                        updateReceipt(
                            receiptId = currentReceiptId,
                            receiptRepository = receiptRepository,
                            receiptFields = receiptFields,
                            state = state
                        )

                        sendEffect(EditReceiptEffect.NavigateBack)
                    }
                }

                is EditReceiptEvent.NavigateBack -> sendEffect(EditReceiptEffect.NavigateBack)
            }
        }
    }

    LaunchedEffect(currentReceiptId) {
        if (currentReceiptId == 0L) return@LaunchedEffect

        getReceipt(
            receiptId = currentReceiptId,
            previousState = state,
            receiptRepository = receiptRepository,
            receiptFields = receiptFields
        )
    }

    LaunchedEffect(hasInvalidField) {
        if (hasInvalidField) sendEffect(EditReceiptEffect.InvalidFieldErrorMessage)
    }

    return EditReceiptState(
        hasInvalidField = hasInvalidField,
        receipt = state.value.receipt
    )
}

private suspend fun updateReceipt(
    receiptId: Long,
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    state: MutableState<EditReceiptState>
) {
    receiptRepository.update(
        data = receiptFields.toReceipt(
            id = receiptId,
            product = state.value.receipt.product
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
    receiptId: Long,
    previousState: MutableState<EditReceiptState>,
    receiptRepository: ReceiptRepository
) {
    receiptRepository.update(
        data = previousState.value.receipt.copy(
            id = receiptId,
            canceled = true
        )
    )
}

private suspend fun getReceipt(
    receiptId: Long,
    previousState: MutableState<EditReceiptState>,
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields
) {
    receiptRepository.get(id = receiptId)
        .catch { it.printStackTrace() }
        .collect {
            previousState.value =
                previousState.value.copy(
                    receipt = it
                )

            receiptFields.setFields(receipt = it)
    }
}