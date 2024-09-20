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
                previousState.copy(editing = true) to ReceiptEffect.UpdateReceipt(id = event.id)
            }

            is ReceiptEvent.IsAdding -> {
                previousState.copy(adding = true) to ReceiptEffect.InsertReceipt
            }

            is ReceiptEvent.UpdateHasInvalidField -> {
                val effect =
                    if (event.hasInvalidField) ReceiptEffect.InvalidFieldErrorMessage
                    else null

                previousState.copy(
                    hasInvalidField = event.hasInvalidField
                ) to effect
            }

            is ReceiptEvent.CancelReceipt -> {
                previousState.copy(canceled = true) to ReceiptEffect.CancelReceipt(id = event.id)
            }

            is ReceiptEvent.UpdateCurrentProduct -> {
                previousState.copy(
                    receipt = previousState.receipt.copy(product = event.product)
                ) to null
            }

            is ReceiptEvent.UpdateCurrentReceipt -> {
                previousState.copy(receipt = event.receipt) to null
            }

            is ReceiptEvent.UpdateDeleteReceipt -> {
                previousState.copy(
                    deleteReceipt = true
                ) to ReceiptEffect.DeleteReceipt(id = event.id)
            }

            is ReceiptEvent.OnUpdateReceiptSuccessfully -> {
                previousState.copy(
                    editing = false,
                    navigateBack = true
                ) to ReceiptEffect.NavigateBack
            }

            is ReceiptEvent.OnAddReceiptSuccessfully -> {
                previousState.copy(
                    adding = false,
                    navigateBack = true
                ) to ReceiptEffect.NavigateBack
            }

            is ReceiptEvent.OnCancelReceiptSuccessfully -> {
                previousState.copy(
                    canceled = false,
                    navigateBack = true
                ) to ReceiptEffect.NavigateBack
            }

            is ReceiptEvent.OnDeleteReceiptSuccessfully -> {
                previousState.copy(
                    deleteReceipt = false,
                    navigateBack = true
                ) to ReceiptEffect.NavigateBack
            }
        }
    }
}