package com.bruno13palhano.receipt.ui.search.presenter

import com.bruno13palhano.ui.shared.ActionProcessor

internal class SearchActionProcessor : ActionProcessor<SearchAction, SearchEvent> {
    override fun processAction(viewAction: SearchAction) : SearchEvent {
        return when (viewAction) {
            is SearchAction.OnDeleteClick -> {
                SearchEvent.Delete(query = viewAction.query)
            }

            is SearchAction.OnActiveChange -> {
                SearchEvent.Active(active = viewAction.active)
            }

            is SearchAction.OnCloseClick -> {
                SearchEvent.Close
            }

            is SearchAction.OnProductItemClick -> {
                SearchEvent.ProductItem(id = viewAction.id)
            }

            is SearchAction.OnDoneClick -> {
                SearchEvent.Done(query = viewAction.query)
            }
        }
    }
}