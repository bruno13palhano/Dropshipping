package com.bruno13palhano.receipt.ui.search

import com.bruno13palhano.ui.shared.Reducer

internal class SearchReducer : Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when (event) {
            is SearchEvent.Close -> {
                if (previousState.active) {
                    previousState.copy(
                        active = false,
                        query = "",
                        insert = false,
                        delete = false
                    ) to null
                } else {
                    previousState to SearchEffect.NavigateBack
                }
            }

            is SearchEvent.ProductItem -> {
                previousState.copy(
                    active = false,
                    query = "",
                    insert = false,
                    delete = false
                ) to SearchEffect.NavigateToAddReceipt(productId = event.id)
            }

            is SearchEvent.Done -> {
                if (event.query.isNotBlank()) {
                    previousState.copy(
                        active = false,
                        query = event.query,
                        insert = true,
                        delete = false
                    ) to null
                } else {
                    previousState to null
                }
            }

            is SearchEvent.Active -> {
                previousState.copy(
                    active = event.active,
                    insert = false,
                    delete = false
                ) to null
            }

            is SearchEvent.Delete -> {
                previousState.copy(
                    query = event.query,
                    delete = true,
                    insert = false,
                    active = true
                ) to null
            }

            is SearchEvent.UpdateCache -> {
                previousState.copy(
                    cache = event.cache,
                    insert = false,
                    delete = false
                ) to null
            }

            is SearchEvent.UpdateProducts -> previousState.copy(products = event.products) to null
        }
    }
}