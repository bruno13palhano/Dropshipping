package com.bruno13palhano.receipt.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class SearchReducer : Reducer<SearchState, SearchEvent, SearchEffect> {
    override fun reduce(
        previousState: SearchState,
        event: SearchEvent
    ): Pair<SearchState, SearchEffect?> {
        return when (event) {
            is SearchEvent.UpdateDeleting -> {
                val effect =
                    if (event.deleting) SearchEffect.DeleteCache(query = event.query)
                    else null

                previousState.copy(
                    deleting = event.deleting
                ) to effect
            }
            is SearchEvent.UpdateQuery -> {
                previousState.copy(
                    query = event.query
                ) to null
            }
            is SearchEvent.UpdateActive -> {
                previousState.copy(
                    updatingCache = false,
                    updatingProducts = false,
                    active = event.active
                ) to null
            }
            is SearchEvent.UpdateProducts -> {
                previousState.copy(
                    updatingProducts = true,
                    products = event.products
                ) to null
            }
            is SearchEvent.UpdateCache -> {
                previousState.copy(
                    updatingCache = true,
                    cache = event.cache
                ) to null
            }
            is SearchEvent.OnSearchDoneClick -> {
                previousState.copy(
                    updatingCache = true,
                    active = false,
                    query = event.query
                ) to SearchEffect.SearchingProducts(query = event.query)
            }
            is SearchEvent.OnCloseSearchClick -> {
                if (previousState.active) {
                    previousState.copy(
                        updatingCache = false,
                        updatingProducts = false,
                        query = "",
                        active = false
                    ) to SearchEffect.RefreshProducts
                } else {
                    previousState.copy(
                        updatingCache = false,
                        updatingProducts = false,
                        navigateBack = true
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