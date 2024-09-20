package com.bruno13palhano.receipt.ui.shared

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState
import kotlinx.coroutines.flow.Flow

@Immutable
internal data class ReceiptsState(
    val receiptsLoading: Boolean,
    val editReceipt: Boolean,
    val searchProduct: Boolean,
    val receipts: Flow<PagingData<Receipt>>
) : ViewState

@Immutable
internal sealed interface ReceiptsEvent : ViewEvent {
    data class UpdateReceipts(val isLoading: Boolean, val receipts: Flow<PagingData<Receipt>>) : ReceiptsEvent
    data class EditReceipt(val editReceipt: Boolean, val id: Long) : ReceiptsEvent
    data class SearchProduct(val searching: Boolean) : ReceiptsEvent
}

@Immutable
internal sealed interface ReceiptsEffect : ViewEffect {
    data class NavigateToEditReceipt(val id: Long) : ReceiptsEffect
    data object NavigateToSearchProduct : ReceiptsEffect
}