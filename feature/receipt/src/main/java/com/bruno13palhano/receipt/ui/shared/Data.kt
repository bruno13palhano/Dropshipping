package com.bruno13palhano.receipt.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class ReceiptsState(
    val productsLoading: Boolean,
    val receiptsLoading: Boolean,
    val cacheLoading: Boolean,
    val editReceipt: Boolean,
    val addReceipt: Boolean,
    val deleteReceipt: Boolean,
    val deleteCache: Boolean,
    val searching: Boolean,
    val products: List<CommonItem>,
    val receipts: List<CommonItem>,
    val cache: List<String>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ReceiptsState(
            productsLoading = false,
            receiptsLoading = false,
            cacheLoading = false,
            editReceipt = false,
            addReceipt = false,
            deleteReceipt = false,
            deleteCache = false,
            searching = false,
            products = emptyList(),
            receipts = emptyList(),
            cache = emptyList()
        )
    }
}

@Immutable
internal sealed interface ReceiptsEvent : Reducer.ViewEvent {
    data class UpdateProducts(val isLoading: Boolean, val products: List<CommonItem>) : ReceiptsEvent
    data class UpdateReceipts(val isLoading: Boolean, val receipts: List<CommonItem>) : ReceiptsEvent
    data class UpdateCache(val isLoading: Boolean, val cache: List<String>) : ReceiptsEvent
    data class EditReceipt(val editReceipt: Boolean, val id: Long) : ReceiptsEvent
    data class UpdateDeletingReceipt(val isDeleting: Boolean, val id: Long) : ReceiptsEvent
    data class UpdateDeletingCache(val isDeleting: Boolean, val query: String) : ReceiptsEvent
    data class UpdateSearching(val isSearching: Boolean) : ReceiptsEvent
    data object OnDeleteReceiptSuccessfully : ReceiptsEvent
    data object OnDeleteCacheSuccessfully : ReceiptsEvent
    data class OnSearchDoneClick(val query: String) : ReceiptsEvent
    data object OnCloseSearchClick : ReceiptsEvent
    data class OnProductItemClick(val id: Long) : ReceiptsEvent
}

@Immutable
internal sealed interface ReceiptsEffect : Reducer.ViewEffect {
    data class NavigateToEditReceipt(val id: Long) : ReceiptsEffect
    data class NavigateToAddReceipt(val productId: Long) : ReceiptsEffect
    data class SearchingProducts(val query: String) : ReceiptsEffect
    data class DeleteReceipt(val id: Long) : ReceiptsEffect
    data class DeleteCache(val query: String) : ReceiptsEffect
    data object ShowDeletedMessage : ReceiptsEffect
}