package com.bruno13palhano.receipt.ui.receipt

import com.bruno13palhano.ui.shared.ActionProcessor

internal class NewReceiptActionProcessor : ActionProcessor<NewReceiptAction, NewReceiptEvent> {
    override fun processAction(viewAction: NewReceiptAction): NewReceiptEvent {
        return when (viewAction) {
            is NewReceiptAction.OnSetInitialData -> {
                NewReceiptEvent.SetInitialData(
                    productId = viewAction.productId,
                    requestDate = viewAction.requestDate
                )
            }

            is NewReceiptAction.OnDoneClick -> NewReceiptEvent.Done

            is NewReceiptAction.OnBackClick -> NewReceiptEvent.NavigateBack
        }
    }
}