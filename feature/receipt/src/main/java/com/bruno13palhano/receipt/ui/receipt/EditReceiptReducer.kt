package com.bruno13palhano.receipt.ui.receipt

import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer

internal class EditReceiptReducer(
    private val receiptFields: ReceiptFields
) : Reducer<EditReceiptState, EditReceiptEvent, EditReceiptEffect> {
    override fun reduce(
        previousState: EditReceiptState,
        event: EditReceiptEvent
    ): Pair<EditReceiptState, EditReceiptEffect?> {
        return when (event) {
            is EditReceiptEvent.UpdateReceipt -> {
                updateReceipt(previousState = previousState, receipt = event.receipt)
            }

            is EditReceiptEvent.Cancel -> {
                previousState.copy(cancel = true) to EditReceiptEffect.NavigateBack
            }

            is EditReceiptEvent.Delete -> {
                previousState.copy(delete = true) to EditReceiptEffect.NavigateBack
            }

            is EditReceiptEvent.Done -> {
                done(previousState = previousState, isValid = receiptFields.isReceiptValid())
            }

            is EditReceiptEvent.NavigateBack -> previousState to EditReceiptEffect.NavigateBack
        }
    }

    private fun updateReceipt(
        previousState: EditReceiptState,
        receipt: Receipt
    ): Pair<EditReceiptState, EditReceiptEffect?> {
        receiptFields.setFields(receipt = receipt)

        return previousState.copy(receipt = receipt) to null
    }

    private fun done(
        previousState: EditReceiptState,
        isValid: Boolean
    ): Pair<EditReceiptState, EditReceiptEffect?> {
        return if(isValid) {
            previousState.copy(
                edit = true,
                hasInvalidField = false
            ) to EditReceiptEffect.NavigateBack
        } else {
            previousState.copy(
                edit = false,
                hasInvalidField = true
            ) to EditReceiptEffect.InvalidFieldErrorMessage
        }
    }
}