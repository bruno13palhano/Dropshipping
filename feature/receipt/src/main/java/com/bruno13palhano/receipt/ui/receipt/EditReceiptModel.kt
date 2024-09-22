package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class ReceiptState(
    val newReceipt: Boolean,
    val hasInvalidField: Boolean,
    val deleteReceipt: Boolean,
    val canceled: Boolean,
    val receipt: Receipt
) : ViewState {
    companion object {
        val INITIAL_STATE = ReceiptState(
            newReceipt = false,
            hasInvalidField = false,
            deleteReceipt = false,
            canceled = false,
            receipt = Receipt.EMPTY
        )
    }
}

@Immutable
internal sealed interface ReceiptEvent : ViewEvent {
    data class UpdateRequestDate(val requestDate: Long): ReceiptEvent
    data class EditReceipt(val id: Long) : ReceiptEvent
    data class AddReceipt(val productId: Long) : ReceiptEvent
    data class CancelReceipt(val id: Long) : ReceiptEvent
    data class DeleteReceipt(val id: Long) : ReceiptEvent
    data object DoneReceipt : ReceiptEvent
    data object NavigateBack : ReceiptEvent
}

@Immutable
internal sealed interface ReceiptEffect : ViewEffect {
    data object InvalidFieldErrorMessage : ReceiptEffect
    data object NavigateBack : ReceiptEffect
}

@Immutable
internal sealed interface ReceiptAction : ViewAction {
    data class OnUpdateRequestDate(val requestDate: Long) : ReceiptAction
    data class OnAddNewReceiptClick(val productId: Long) : ReceiptAction
    data class OnEditReceiptClick(val id: Long) : ReceiptAction
    data class OnCancelReceiptClick(val id: Long) : ReceiptAction
    data class OnDeleteReceiptClick(val id: Long) : ReceiptAction
    data object OnDoneReceiptClick : ReceiptAction
    data object OnBackClick : ReceiptAction
}