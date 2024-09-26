package com.bruno13palhano.receipt.ui.search

import com.bruno13palhano.ui.shared.Reducer

internal class SearchReducer : Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when (event) {
            is SearchEvent.Delete -> {
                previousState to null
            }

            is SearchEvent.Active -> {
                previousState to null
            }

            is SearchEvent.Done -> {
                previousState to null
            }

            is SearchEvent.Close -> {
                previousState to null
            }

            is SearchEvent.ProductItem -> {
                previousState to SearchEffect.NavigateToAddReceipt(event.id)
            }
        }
    }
}