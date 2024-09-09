package com.bruno13palhano.receipt.ui.shared

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
                    deleteReceipt = false,
                    receipts = event.receipts
                ) to null
            }
            is ReceiptsEvent.EditReceipt -> {
                previousState.copy(
                    receiptsLoading = false,
                    editReceipt = event.editReceipt,
                    searchProduct = false,
                    deleteReceipt = false,
                ) to ReceiptsEffect.NavigateToEditReceipt(id = event.id)
            }
            is ReceiptsEvent.SearchProduct -> {
                previousState.copy(
                    receiptsLoading = false,
                    editReceipt = false,
                    searchProduct = event.searching,
                    deleteReceipt = false
                ) to ReceiptsEffect.NavigateToSearchProduct
            }
            is ReceiptsEvent.UpdateDeletingReceipt -> {
                previousState.copy(
                    receiptsLoading = false,
                    editReceipt = false,
                    searchProduct = false,
                    deleteReceipt = event.isDeleting
                ) to ReceiptsEffect.DeleteReceipt(id = event.id)
            }
            is ReceiptsEvent.OnDeleteReceiptSuccessfully -> {
                previousState.copy(deleteReceipt = false) to ReceiptsEffect.ShowDeletedMessage
            }
        }
    }
}