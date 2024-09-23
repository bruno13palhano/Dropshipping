package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class EditReceiptState(
    val hasInvalidField: Boolean,
    val receipt: Receipt
) : ViewState {
    companion object {
        val INITIAL_STATE = EditReceiptState(
            hasInvalidField = false,
            receipt = Receipt.EMPTY
        )
    }
}

@Immutable
internal sealed interface EditReceiptEvent : ViewEvent {
    data class SetInitialData(val id: Long) : EditReceiptEvent
    data class Cancel(val id: Long) : EditReceiptEvent
    data class Delete(val id: Long) : EditReceiptEvent
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
    data class OnSetInitialData(val id: Long) : EditReceiptAction
    data class OnCancelClick(val id: Long) : EditReceiptAction
    data class OnDeleteClick(val id: Long) : EditReceiptAction
    data object OnDoneClick : EditReceiptAction
    data object OnBackClick : EditReceiptAction
}