package com.bruno13palhano.receipt.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class ReceiptsState(
    val receiptsLoading: Boolean,
    val editReceipt: Boolean,
    val searchProduct: Boolean,
    val deleteReceipt: Boolean,
    val receipts: List<CommonItem>,
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ReceiptsState(
            receiptsLoading = false,
            editReceipt = false,
            searchProduct = false,
            deleteReceipt = false,
            receipts = emptyList()
        )
    }
}

@Immutable
internal sealed interface ReceiptsEvent : Reducer.ViewEvent {
    data class UpdateProducts(val isLoading: Boolean, val products: List<CommonItem>) : ReceiptsEvent
    data class UpdateReceipts(val isLoading: Boolean, val receipts: List<CommonItem>) : ReceiptsEvent
    data class EditReceipt(val editReceipt: Boolean, val id: Long) : ReceiptsEvent
    data class SearchProduct(val searching: Boolean) : ReceiptsEvent
    data class UpdateDeletingReceipt(val isDeleting: Boolean, val id: Long) : ReceiptsEvent
    data object OnDeleteReceiptSuccessfully : ReceiptsEvent
}

@Immutable
internal sealed interface ReceiptsEffect : Reducer.ViewEffect {
    data class NavigateToEditReceipt(val id: Long) : ReceiptsEffect
    data object NavigateToSearchProduct : ReceiptsEffect
    data class DeleteReceipt(val id: Long) : ReceiptsEffect
    data object ShowDeletedMessage : ReceiptsEffect
}

@Immutable
internal data class SearchState(
    val deleting: Boolean,
    val updatingCache: Boolean,
    val updatingProducts: Boolean,
    val query: String,
    val active: Boolean,
    val navigateBack: Boolean,
    val products: List<CommonItem>,
    val cache: List<String>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = SearchState(
            deleting = false,
            updatingCache = false,
            updatingProducts = false,
            query = "",
            active = false,
            navigateBack = false,
            products = emptyList(),
            cache = emptyList()
        )
    }
}

@Immutable
internal sealed interface SearchEvent : Reducer.ViewEvent {
    data class UpdateDeleting(val deleting: Boolean, val query: String) : SearchEvent
    data class UpdateQuery(val query: String) : SearchEvent
    data class UpdateActive(val active: Boolean) : SearchEvent
    data class UpdateProducts(val products: List<CommonItem>) : SearchEvent
    data class UpdateCache(val cache: List<String>) : SearchEvent
    data class OnSearchDoneClick(val query: String) : SearchEvent
    data object OnCloseSearchClick : SearchEvent
    data class OnProductItemClick(val id: Long) : SearchEvent
}

@Immutable
internal sealed interface SearchEffect : Reducer.ViewEffect {
    data class NavigateToAddReceipt(val productId: Long) : SearchEffect
    data object NavigateBack : SearchEffect
    data class SearchingProducts(val query: String) : SearchEffect
    data class DeleteCache(val query: String) : SearchEffect
    data object RefreshProducts : SearchEffect
}