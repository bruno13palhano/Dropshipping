package com.bruno13palhano.receipt.ui.receipt.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class EditReceiptState(
    val edit: Boolean,
    val cancel: Boolean,
    val delete: Boolean,
    val hasInvalidField: Boolean,
    val receipt: Receipt
) : ViewState {
    companion object {
        val INITIAL_STATE = EditReceiptState(
            edit = false,
            cancel = false,
            delete = false,
            hasInvalidField = false,
            receipt = Receipt.EMPTY
        )
    }
}

@Immutable
internal sealed interface EditReceiptEvent : ViewEvent {
    data class UpdateReceipt(val receipt: Receipt) : EditReceiptEvent
    data object Cancel : EditReceiptEvent
    data object Delete : EditReceiptEvent
    data object Done : EditReceiptEvent
    data object NavigateBack : EditReceiptEvent
}

@Immutable
internal sealed interface EditReceiptEffect : ViewEffect {
    data object InvalidFieldErrorMessage : EditReceiptEffect
    data object NavigateBack : EditReceiptEffect
}

@Immutable
internal sealed interface EditReceiptAction : ViewAction {
    data class OnUpdateReceipt(val id: Long) : EditReceiptAction
    data object OnCancelClick : EditReceiptAction
    data object OnDeleteClick : EditReceiptAction
    data object OnDoneClick : EditReceiptAction
    data object OnBackClick : EditReceiptAction
}