package com.bruno13palhano.receipt.ui.search

import com.bruno13palhano.ui.shared.Reducer

internal class SearchReducer : Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when (event) {
            is SearchEvent.UpdateDeleting -> {
                previousState.copy(
                    deleting = event.deleting
                ) to null
            }

            is SearchEvent.UpdateActive -> {
                previousState.copy(
                    updatingCache = false,
                    updatingProducts = false,
                    active = event.active
                ) to null
            }

            is SearchEvent.OnSearchDoneClick -> {
                previousState.copy(
                    updatingCache = true,
                    active = false,
                    query = event.query
                ) to null
            }

            is SearchEvent.OnCloseSearchClick -> {
                if (previousState.active) {
                    previousState.copy(
                        updatingCache = false,
                        updatingProducts = false,
                        query = "",
                        active = false
                    ) to null
                } else {
                    previousState.copy(
                        updatingCache = false,
                        updatingProducts = false
                    ) to SearchEffect.NavigateBack
                }
            }

            is SearchEvent.OnProductItemClick -> {
                previousState.copy(
                    active = false
                ) to SearchEffect.NavigateToAddReceipt(event.id)
            }
        }
    }
}