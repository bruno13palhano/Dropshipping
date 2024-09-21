package com.bruno13palhano.receipt.ui.receipts

import com.bruno13palhano.ui.shared.ActionProcessor

internal class ReceiptsActionProcessor : ActionProcessor<ReceiptsAction, ReceiptsEvent> {
    override fun processAction(viewAction: ReceiptsAction): ReceiptsEvent {
        return when (viewAction) {
            is ReceiptsAction.OnEditReceiptClick -> {
                ReceiptsEvent.EditReceipt(editReceipt = viewAction.editReceipt, id = viewAction.id)
            }

            is ReceiptsAction.OnSearchProductClick -> {
                ReceiptsEvent.SearchProduct(searching = viewAction.searching)
            }
        }
    }
}