package com.bruno13palhano.receipt.ui.shared

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
                ) to SearchEffect.DeleteCache(query = event.query)
            }
            is SearchEvent.UpdateQuery -> {
                previousState.copy(query = event.query) to null
            }
            is SearchEvent.UpdateActive -> {
                previousState.copy(active = event.active) to null
            }
            is SearchEvent.UpdateProducts -> {
                previousState.copy(products = event.products) to null
            }
            is SearchEvent.UpdateCache -> {
                previousState.copy(cache = event.cache) to null
            }
            is SearchEvent.OnDeleteCacheSuccessfully -> {
                previousState.copy(deleting = false) to null
            }
            is SearchEvent.OnSearchDoneClick -> {
                previousState.copy(
                    updatingCache = true,
                    active = false
                ) to SearchEffect.SearchingProducts(query = event.query)
            }
            is SearchEvent.OnCloseSearchClick -> {
                previousState.copy(
                    active = false
                ) to SearchEffect.RefreshProducts
            }
            is SearchEvent.OnProductItemClick -> {
                previousState.copy(active = false) to SearchEffect.NavigateToAddReceipt(event.id)
            }
            is SearchEvent.OnNavigateBackClick -> {
                previousState.copy(navigateBack = true) to SearchEffect.NavigateBack
            }
        }
    }
}