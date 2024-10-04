package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class NewProductState(
    val insert: Boolean,
    val hasInvalidField: Boolean
) : ViewState {
    companion object {
        val INITIAL_STATE = NewProductState(insert = false, hasInvalidField = false)
    }
}

@Immutable
internal sealed interface NewProductEvent : ViewEvent {
    data object Done : NewProductEvent
    data object NavigateBack : NewProductEvent
}

@Immutable
internal sealed interface NewProductEffect : ViewEffect {
    data object InvalidFieldErrorMessage : NewProductEffect
    data object NavigateBack : NewProductEffect
}

@Immutable
internal sealed interface NewProductAction : ViewAction {
    data object OnDoneClick : NewProductAction
    data object OnBackClick : NewProductAction
}