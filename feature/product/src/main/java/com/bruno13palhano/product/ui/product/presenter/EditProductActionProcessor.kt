package com.bruno13palhano.product.ui.product.presenter

import com.bruno13palhano.ui.shared.ActionProcessor

internal class EditProductActionProcessor : ActionProcessor<EditProductAction, EditProductEvent> {
    override fun processAction(viewAction: EditProductAction): EditProductEvent {
        return when (viewAction) {
            is EditProductAction.OnSetInitialData -> {
                EditProductEvent.SetInitialData(id = viewAction.id)
            }

            is EditProductAction.OnDeleteClick -> EditProductEvent.DeleteEditProduct

            is EditProductAction.OnDoneClick -> EditProductEvent.Done

            is EditProductAction.OnBackClick -> EditProductEvent.NavigateBack
        }
    }
}