package com.bruno13palhano.receipt.ui.shared

import com.bruno13palhano.receipt.ui.receipts.ReceiptsEffect
import com.bruno13palhano.receipt.ui.receipts.ReceiptsEvent
import com.bruno13palhano.receipt.ui.receipts.ReceiptsState
import com.bruno13palhano.ui.shared.Reducer

internal class ReceiptsReducer : Reducer<ReceiptsState, ReceiptsEvent, ReceiptsEffect> {
    override fun reduce(
        previousState: ReceiptsState,
        event: ReceiptsEvent
    ): Pair<ReceiptsState, ReceiptsEffect?> {
        return when (event) {
            is ReceiptsEvent.UpdateReceipts -> {
                previousState.copy(
                    receiptsLoading = event.isLoading,
                    editReceipt = false,
                    searchProduct = false,
                    receipts = event.receipts
                ) to null
            }
            is ReceiptsEvent.EditReceipt -> {
                previousState.copy(
                    receiptsLoading = false,
                    editReceipt = event.editReceipt,
                    searchProduct = false
                ) to ReceiptsEffect.NavigateToEditReceipt(id = event.id)
            }
            is ReceiptsEvent.SearchProduct -> {
                previousState.copy(
                    receiptsLoading = false,
                    editReceipt = false,
                    searchProduct = event.searching
                ) to ReceiptsEffect.NavigateToSearchProduct
            }
        }
    }
}