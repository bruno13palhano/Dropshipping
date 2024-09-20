package com.bruno13palhano.receipt.ui.search

import com.bruno13palhano.ui.shared.ActionProcessor

internal class SearchActionProcessor : ActionProcessor<SearchAction, SearchEvent> {
    override fun processAction(viewAction: SearchAction) : SearchEvent {
        return when (viewAction) {
            is SearchAction.DeleteSearchClick -> {
                SearchEvent.UpdateDeleting(deleting = viewAction.deleting, query = viewAction.query)
            }

            is SearchAction.OnActiveChange -> {
                SearchEvent.UpdateActive(active = viewAction.active)
            }

            is SearchAction.OnCloseSearchClick -> {
                SearchEvent.OnCloseSearchClick
            }

            is SearchAction.OnProductItemClick -> {
                SearchEvent.OnProductItemClick(id = viewAction.id)
            }

            is SearchAction.OnSearchDoneClick -> {
                SearchEvent.OnSearchDoneClick(query = viewAction.query)
            }
        }
    }
}