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