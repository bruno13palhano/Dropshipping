package com.bruno13palhano.receipt.ui.shared

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
internal data class ReceiptsState(
    val receiptsLoading: Boolean,
    val editReceipt: Boolean,
    val searchProduct: Boolean,
    val receipts: Flow<PagingData<Receipt>>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ReceiptsState(
            receiptsLoading = false,
            editReceipt = false,
            searchProduct = false,
            receipts = emptyFlow()
        )
    }
}

@Immutable
internal sealed interface ReceiptsEvent : Reducer.ViewEvent {
    data class UpdateReceipts(val isLoading: Boolean, val receipts: Flow<PagingData<Receipt>>) : ReceiptsEvent
    data class EditReceipt(val editReceipt: Boolean, val id: Long) : ReceiptsEvent
    data class SearchProduct(val searching: Boolean) : ReceiptsEvent
}

@Immutable
internal sealed interface ReceiptsEffect : Reducer.ViewEffect {
    data class NavigateToEditReceipt(val id: Long) : ReceiptsEffect
    data object NavigateToSearchProduct : ReceiptsEffect
}