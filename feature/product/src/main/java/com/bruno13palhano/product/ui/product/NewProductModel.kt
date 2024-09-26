package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class NewProductState(val hasInvalidField: Boolean) : ViewState

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