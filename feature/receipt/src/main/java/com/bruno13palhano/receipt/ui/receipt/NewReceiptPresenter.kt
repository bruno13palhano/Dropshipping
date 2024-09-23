package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

@Composable
internal fun newReceiptPresenter(
    receiptFields: ReceiptFields,
    productRepository: ProductRepository,
    receiptRepository: ReceiptRepository,
    events: Flow<NewReceiptEvent>,
    sendEffect: (effect: NewReceiptEffect) -> Unit
): NewReceiptState {
    val state = remember { mutableStateOf(NewReceiptState.INITIAL_STATE) }
    var hasInvalidField by remember { mutableStateOf(false) }
    var currentProductId by remember { mutableLongStateOf(0L) }
    var done by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NewReceiptEvent.SetInitialData -> {
                    currentProductId = event.productId
                    receiptFields.updateRequestDate(requestDate = event.requestDate)
                }

                is NewReceiptEvent.Done -> {
                    if (!receiptFields.isReceiptValid()) {
                        hasInvalidField = true
                    } else {
                        hasInvalidField = false
                        done = true
                    }
                }

                is NewReceiptEvent.NavigateBack -> {
                    sendEffect(NewReceiptEffect.NavigateBack)
                }
            }
        }
    }

    LaunchedEffect(currentProductId) {
        if (currentProductId == 0L) return@LaunchedEffect

        getProduct(
            productId = currentProductId,
            previousState = state,
            productRepository = productRepository,
            receiptFields = receiptFields
        )
    }

    LaunchedEffect(hasInvalidField) {
        if (hasInvalidField) sendEffect(NewReceiptEffect.InvalidFieldErrorMessage)
    }

    LaunchedEffect(done) {
        if (done) {
            insertReceipt(
                receiptRepository = receiptRepository,
                receiptFields = receiptFields,
                state = state
            )

            sendEffect(NewReceiptEffect.NavigateBack)
        }
    }

    return NewReceiptState(
        hasInvalidField = hasInvalidField,
        receipt = state.value.receipt
    )
}

private suspend fun insertReceipt(
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    state: MutableState<NewReceiptState>
) {
    receiptRepository.insert(
        data = receiptFields.toReceipt(product = state.value.receipt.product)
    )
}

private suspend fun getProduct(
    productId: Long,
    previousState: MutableState<NewReceiptState>,
    productRepository: ProductRepository,
    receiptFields: ReceiptFields
) {
    productRepository.get(id = productId).collect {
        previousState.value =
            previousState.value.copy(
                receipt = previousState.value.receipt.copy(
                    product = it
                )
            )

        receiptFields.updateProductName(productName = it.name)
    }
}