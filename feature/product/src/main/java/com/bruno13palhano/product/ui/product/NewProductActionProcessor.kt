package com.bruno13palhano.product.ui.product

import com.bruno13palhano.ui.shared.ActionProcessor

internal class NewProductActionProcessor : ActionProcessor<NewProductAction, NewProductEvent> {
    override fun processAction(viewAction: NewProductAction): NewProductEvent {
        return when (viewAction) {
            is NewProductAction.OnDoneClick -> NewProductEvent.Done

            is NewProductAction.OnBackClick -> NewProductEvent.NavigateBack
        }
    }
}