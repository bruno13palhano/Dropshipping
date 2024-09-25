package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class ReceiptsState(val receipts: List<Receipt>) : ViewState

@Immutable
internal sealed interface ReceiptsEvent : ViewEvent {
    data class EditReceipt(val id: Long) : ReceiptsEvent
    data object SearchProduct : ReceiptsEvent
}

@Immutable
internal sealed interface ReceiptsEffect : ViewEffect {
    data class NavigateToEditReceipt(val id: Long) : ReceiptsEffect
    data object NavigateToSearchProduct : ReceiptsEffect
}

@Immutable
internal sealed interface ReceiptsAction : ViewAction  {
    data class OnEditReceiptClick(val id: Long) : ReceiptsAction
    data object OnSearchProductClick : ReceiptsAction
}