package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class NewReceiptState(
    val insert: Boolean,
    val hasInvalidField: Boolean,
    val product: Product
) : ViewState {
    companion object {
        val INITIAL_STATE = NewReceiptState(
            insert = false,
            hasInvalidField = false,
            product = Product.EMPTY
        )
    }
}

@Immutable
internal sealed interface NewReceiptEvent : ViewEvent {
    data class SetInitialData(val product: Product, val requestDate: Long): NewReceiptEvent
    data object Done : NewReceiptEvent
    data class UpdateProduct(val product: Product) : NewReceiptEvent
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