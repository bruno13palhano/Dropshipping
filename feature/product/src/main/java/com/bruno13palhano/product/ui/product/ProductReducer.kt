package com.bruno13palhano.product.ui.product

import com.bruno13palhano.ui.shared.Reducer

internal class ProductReducer : Reducer<EditProductState, EditProductEvent, EditProductEffect> {
    override fun reduce(
        previousState: EditProductState,
        event: EditProductEvent
    ): Pair<EditProductState, EditProductEffect?> {
        return when (event) {
            is EditProductEvent.NavigateBack -> {
                previousState to EditProductEffect.NavigateBack
            }

            is EditProductEvent.DeleteEditProduct -> {
                previousState to null
            }

            is EditProductEvent.Done -> {
                previousState to null
            }

            is EditProductEvent.SetInitialData -> {
                previousState to null
            }
        }
    }
}