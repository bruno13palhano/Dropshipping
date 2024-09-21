package com.bruno13palhano.receipt.ui.receipts

import com.bruno13palhano.ui.shared.Reducer

internal class ReceiptsReducer : Reducer<ReceiptsState, ReceiptsEvent, ReceiptsEffect> {
    override fun reduce(
        previousState: ReceiptsState,
        event: ReceiptsEvent
    ): Pair<ReceiptsState, ReceiptsEffect?> {
        return when (event) {
            is ReceiptsEvent.EditReceipt -> {
                previousState.copy(
                    editReceipt = event.editReceipt,
                    searchProduct = false
                ) to ReceiptsEffect.NavigateToEditReceipt(id = event.id)
            }
            is ReceiptsEvent.SearchProduct -> {
                previousState.copy(
                    editReceipt = false,
                    searchProduct = event.searching
                ) to ReceiptsEffect.NavigateToSearchProduct
            }
        }
    }
}