package com.bruno13palhano.receipt.ui.receipt.presenter

import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ActionProcessor

internal class EditReceiptActionProcessor : ActionProcessor<EditReceiptAction, EditReceiptEvent> {
    override fun processAction(viewAction: EditReceiptAction): EditReceiptEvent {
        return when (viewAction) {
            is EditReceiptAction.OnUpdateReceipt -> {
                EditReceiptEvent.UpdateReceipt(receipt = Receipt.EMPTY.copy(id = viewAction.id))
            }

            is EditReceiptAction.OnCancelClick -> EditReceiptEvent.Cancel

            is EditReceiptAction.OnDeleteClick -> EditReceiptEvent.Delete

            is EditReceiptAction.OnDoneClick -> EditReceiptEvent.Done

            is EditReceiptAction.OnBackClick -> EditReceiptEvent.NavigateBack
        }
    }
}