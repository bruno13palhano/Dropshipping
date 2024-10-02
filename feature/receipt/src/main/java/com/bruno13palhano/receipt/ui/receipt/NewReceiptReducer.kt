package com.bruno13palhano.receipt.ui.receipt

import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.shared.Reducer

internal class NewReceiptReducer(
    private val receiptFields: ReceiptFields
) : Reducer<NewReceiptState, NewReceiptEvent, NewReceiptEffect> {
    override fun reduce(
        previousState: NewReceiptState,
        event: NewReceiptEvent
    ): Pair<NewReceiptState, NewReceiptEffect?> {
        return when (event) {
            is NewReceiptEvent.SetInitialData -> {
                setInitialState(
                    previousState = previousState,
                    requestDate = event.requestDate,
                    product = event.product
                )
            }

            is NewReceiptEvent.Done -> {
                done(previousState = previousState, isValid = receiptFields.isReceiptValid())
            }

            is NewReceiptEvent.UpdateProduct -> {
                updateProduct(previousState = previousState, product = event.product)
            }

            is NewReceiptEvent.NavigateBack -> previousState to NewReceiptEffect.NavigateBack
        }
    }

    private fun setInitialState(
        previousState: NewReceiptState,
        requestDate: Long,
        product: Product
    ): Pair<NewReceiptState, NewReceiptEffect?> {
        return if (receiptFields.requestDate == 0L) {
            receiptFields.updateRequestDate(requestDate = requestDate)

            previousState.copy(product = product) to null
        } else {
            previousState to null
        }
    }

    private fun done(
        previousState: NewReceiptState,
        isValid: Boolean
    ): Pair<NewReceiptState, NewReceiptEffect?> {
        return if(isValid) {
            previousState.copy(
                insert = true,
                hasInvalidField = false
            ) to null
        } else {
            previousState.copy(
                insert = false,
                hasInvalidField = true
            ) to NewReceiptEffect.InvalidFieldErrorMessage
        }
    }

    private fun updateProduct(
        previousState: NewReceiptState,
        product: Product
    ): Pair<NewReceiptState, NewReceiptEffect?> {
        receiptFields.updateProductName(productName = product.name)

        return previousState.copy(product = product) to null
    }
}