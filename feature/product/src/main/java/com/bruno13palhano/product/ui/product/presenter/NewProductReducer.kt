package com.bruno13palhano.product.ui.product.presenter

import com.bruno13palhano.ui.shared.Reducer

internal class NewProductReducer(
    private val productFields: ProductFields
) : Reducer<NewProductState, NewProductEvent, NewProductEffect> {
    override fun reduce(
        previousState: NewProductState,
        event: NewProductEvent
    ): Pair<NewProductState, NewProductEffect?> {
        return when (event) {
            is NewProductEvent.Done -> {
                if(productFields.isValid()) {
                    previousState.copy(
                        insert = true,
                        hasInvalidField = false
                    ) to NewProductEffect.NavigateBack
                } else {
                    previousState.copy(
                        insert = false,
                        hasInvalidField = true
                    ) to NewProductEffect.InvalidFieldErrorMessage
                }
            }

            is NewProductEvent.NavigateBack -> {
                previousState to NewProductEffect.NavigateBack
            }
        }
    }
}