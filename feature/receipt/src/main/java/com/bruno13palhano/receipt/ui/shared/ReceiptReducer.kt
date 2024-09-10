package com.bruno13palhano.receipt.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ReceiptReducer : Reducer<ReceiptState, ReceiptEvent, ReceiptEffect> {
    override fun reduce(
        previousState: ReceiptState,
        event: ReceiptEvent
    ): Pair<ReceiptState, ReceiptEffect?> {
        return when (event) {
            is ReceiptEvent.EditReceipt -> {
                previousState.copy(editReceipt = true) to null
            }
            is ReceiptEvent.NewReceipt -> {
                previousState.copy(newReceipt = true) to null
            }
            is ReceiptEvent.IsEditing -> {
                previousState.copy(editing = true) to null
            }
            is ReceiptEvent.IsAdding -> {
                previousState.copy(adding = true) to null
            }
            is ReceiptEvent.UpdateHasInvalidField -> {
                previousState.copy(hasInvalidField = event.hasInvalidField) to null
            }
            is ReceiptEvent.UpdateCanceled -> {
                previousState.copy(canceled = event.canceled) to ReceiptEffect.NavigateBack
            }
            is ReceiptEvent.UpdateProduct -> {
                previousState.copy(
                    receipt = previousState.receipt.copy(product = event.product)
                ) to null
            }
            is ReceiptEvent.UpdateReceipt -> {
                previousState.copy(
                    receipt = event.receipt
                ) to null
            }
            is ReceiptEvent.UpdateDeleteReceipt -> {
                previousState.copy(
                    deleteReceipt = true
                ) to ReceiptEffect.DeleteReceipt(id = event.id)
            }
            is ReceiptEvent.OnDeleteReceiptSuccessfully -> {
                previousState.copy(deleteReceipt = false) to ReceiptEffect.NavigateBack
            }
            is ReceiptEvent.OnNavigateBack -> {
                previousState.copy(navigateBack = true) to ReceiptEffect.NavigateBack
            }
        }
    }
}