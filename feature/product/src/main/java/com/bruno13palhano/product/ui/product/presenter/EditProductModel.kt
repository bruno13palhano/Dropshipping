package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class EditProductState(
    val update: Boolean,
    val delete: Boolean,
    val hasInvalidField: Boolean,
    val id: Long
) : ViewState {
    companion object {
        val INITIAL_STATE = EditProductState(
            update = false,
            delete = false,
            hasInvalidField = false,
            id = 0L
        )
    }
}

@Immutable
internal sealed interface EditProductEvent : ViewEvent {
    data class SetInitialData(val id: Long) : EditProductEvent
    data object DeleteEditProduct : EditProductEvent
    data object Done : EditProductEvent
    data object NavigateBack : EditProductEvent
}

@Immutable
internal sealed interface EditProductEffect : ViewEffect {
    data object InvalidFieldErrorMessage : EditProductEffect
    data object NavigateBack : EditProductEffect
}

@Immutable
internal sealed interface EditProductAction : ViewAction {
    data class OnSetInitialData(val id: Long) : EditProductAction
    data object OnDeleteClick : EditProductAction
    data object OnDoneClick : EditProductAction
    data object OnBackClick : EditProductAction
}