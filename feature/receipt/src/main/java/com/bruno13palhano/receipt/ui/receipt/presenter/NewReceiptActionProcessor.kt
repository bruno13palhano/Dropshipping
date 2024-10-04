package com.bruno13palhano.receipt.ui.receipt.presenter

import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.shared.ActionProcessor

internal class NewReceiptActionProcessor : ActionProcessor<NewReceiptAction, NewReceiptEvent> {
    override fun processAction(viewAction: NewReceiptAction): NewReceiptEvent {
        return when (viewAction) {
            is NewReceiptAction.OnSetInitialData -> {
                NewReceiptEvent.SetInitialData(
                    product = Product.EMPTY.copy(id = viewAction.productId),
                    requestDate = viewAction.requestDate
                )
            }

            is NewReceiptAction.OnDoneClick -> NewReceiptEvent.Done

            is NewReceiptAction.OnBackClick -> NewReceiptEvent.NavigateBack
        }
    }
}