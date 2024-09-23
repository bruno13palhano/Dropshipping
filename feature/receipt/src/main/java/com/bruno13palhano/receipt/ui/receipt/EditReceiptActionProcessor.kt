package com.bruno13palhano.receipt.ui.receipt

import com.bruno13palhano.ui.shared.ActionProcessor

internal class EditReceiptActionProcessor : ActionProcessor<EditReceiptAction, EditReceiptEvent> {
    override fun processAction(viewAction: EditReceiptAction): EditReceiptEvent {
        return when (viewAction) {
            is EditReceiptAction.OnSetInitialData -> {
                EditReceiptEvent.SetInitialData(id = viewAction.id)
            }

            is EditReceiptAction.OnCancelClick -> {
                EditReceiptEvent.Cancel(id = viewAction.id)
            }

            is EditReceiptAction.OnDeleteClick -> {
                EditReceiptEvent.Delete(id = viewAction.id)
            }

            is EditReceiptAction.OnDoneClick -> {
                EditReceiptEvent.Done
            }

            is EditReceiptAction.OnBackClick -> {
                EditReceiptEvent.NavigateBack
            }
        }
    }
}