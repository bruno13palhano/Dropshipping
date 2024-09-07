package com.bruno13palhano.receipt.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ReceiptsReducer : Reducer<ReceiptsState, ReceiptsEvent, ReceiptsEffect> {
    override fun reduce(
        previousState: ReceiptsState,
        event: ReceiptsEvent
    ): Pair<ReceiptsState, ReceiptsEffect?> {
        return when (event) {
            is ReceiptsEvent.UpdateProducts -> {
                previousState.copy(
                    productsLoading = event.isLoading,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = false,
                    products = event.products
                ) to null
            }
            is ReceiptsEvent.UpdateReceipts -> {
                previousState.copy(
                    receiptsLoading = event.isLoading,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = false,
                    receipts = event.receipts
                ) to null
            }
            is ReceiptsEvent.UpdateCache -> {
                previousState.copy(
                    cacheLoading = event.isLoading,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = false,
                    cache = event.cache
                ) to null
            }
            is ReceiptsEvent.EditReceipt -> {
                previousState.copy(
                    productsLoading = false,
                    receiptsLoading = false,
                    cacheLoading = false,
                    editReceipt = event.editReceipt,
                    addReceipt = false,
                    deleteReceipt = false,
                    searching = false
                ) to ReceiptsEffect.NavigateToEditReceipt(id = event.id)
            }
            is ReceiptsEvent.UpdateDeletingReceipt -> {
                previousState.copy(
                    productsLoading = false,
                    receiptsLoading = false,
                    cacheLoading = false,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = event.isDeleting
                ) to ReceiptsEffect.DeleteReceipt(id = event.id)
            }
            is ReceiptsEvent.UpdateDeletingCache -> {
                previousState.copy(
                    productsLoading = false,
                    receiptsLoading = false,
                    cacheLoading = false,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = event.isDeleting
                ) to ReceiptsEffect.DeleteCache(query = event.query)
            }
            is ReceiptsEvent.UpdateSearching -> {
                previousState.copy(
                    productsLoading = false,
                    receiptsLoading = false,
                    cacheLoading = false,
                    editReceipt = false,
                    addReceipt = false,
                    deleteReceipt = false,
                    searching = event.isSearching
                ) to null
            }
            is ReceiptsEvent.OnDeleteReceiptSuccessfully -> {
                previousState.copy(deleteReceipt = false) to ReceiptsEffect.ShowDeletedMessage
            }
            is ReceiptsEvent.OnDeleteCacheSuccessfully -> {
                previousState.copy(deleteReceipt = false) to null
            }
            is ReceiptsEvent.OnSearchDoneClick -> {
                previousState.copy(
                    productsLoading = true
                ) to ReceiptsEffect.SearchingProducts(query = event.query)
            }
            is ReceiptsEvent.OnProductItemClick -> {
                previousState.copy(
                    productsLoading = false,
                    receiptsLoading = false,
                    cacheLoading = false,
                    editReceipt = false,
                    addReceipt = true,
                    deleteReceipt = false,
                    searching = false
                ) to ReceiptsEffect.NavigateToAddReceipt(productId = event.id)
            }
            is ReceiptsEvent.OnCloseSearchClick -> {
                previousState.copy(
                    searching = false
                ) to ReceiptsEffect.RefreshProducts
            }
        }
    }
}