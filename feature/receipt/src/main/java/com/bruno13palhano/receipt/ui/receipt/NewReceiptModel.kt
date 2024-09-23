package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class NewReceiptState(
    val hasInvalidField: Boolean,
    val receipt: Receipt
) : ViewState {
    companion object {
        val INITIAL_STATE = NewReceiptState(
            hasInvalidField = false,
            receipt = Receipt.EMPTY
        )
    }
}

@Immutable
internal sealed interface NewReceiptEvent : ViewEvent {
    data class SetInitialData(val productId: Long, val requestDate: Long): NewReceiptEvent
    data object Done : NewReceiptEvent
    data object NavigateBack : NewReceiptEvent

}

@Immutable
internal sealed interface NewReceiptEffect : ViewEffect {
    data object InvalidFieldErrorMessage : NewReceiptEffect
    data object NavigateBack : NewReceiptEffect
}

@Immutable
internal sealed interface NewReceiptAction : ViewAction {
    data class OnSetInitialData(val productId: Long, val requestDate: Long) : NewReceiptAction
    data object OnDoneClick : NewReceiptAction
    data object OnBackClick : NewReceiptAction
}