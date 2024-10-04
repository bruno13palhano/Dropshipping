package com.bruno13palhano.product.ui.product.presenter

import com.bruno13palhano.ui.shared.Reducer

internal class EditProductReducer(
    private val productFields: ProductFields
) : Reducer<EditProductState, EditProductEvent, EditProductEffect> {
    override fun reduce(
        previousState: EditProductState,
        event: EditProductEvent
    ): Pair<EditProductState, EditProductEffect?> {
        return when (event) {
            is EditProductEvent.SetInitialData -> {
                previousState.copy(id = event.id) to null
            }

            is EditProductEvent.DeleteEditProduct -> {
                previousState.copy(delete = true) to EditProductEffect.NavigateBack
            }

            is EditProductEvent.Done -> {
                if(productFields.isValid()) {
                    previousState.copy(
                        update = true,
                        hasInvalidField = false
                    ) to EditProductEffect.NavigateBack
                } else {
                    previousState.copy(
                        update = false,
                        hasInvalidField = true
                    ) to EditProductEffect.InvalidFieldErrorMessage
                }
            }

            is EditProductEvent.NavigateBack -> {
                previousState to EditProductEffect.NavigateBack
            }
        }
    }
}