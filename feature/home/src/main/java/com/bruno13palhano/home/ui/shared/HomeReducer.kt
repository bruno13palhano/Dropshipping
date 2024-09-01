package com.bruno13palhano.home.ui.shared

import com.bruno13palhano.ui.shared.Reducer

class HomeReducer : Reducer<HomeState, HomeEvent, HomeEffect> {
    override fun reduce(
        state: HomeState,
        event: HomeEvent
    ): Pair<HomeState, HomeEffect?> {
        return when (event) {
            is HomeEvent.UpdateProfitVisibility -> {
                state.copy(profitVisible = event.visible) to null
            }
            is HomeEvent.UpdateProfit -> {
                state.copy(profit = event.profit) to null
            }
            is HomeEvent.UpdateReceiptsVisibility -> {
                state.copy(receiptsVisible = event.visible) to null
            }
            is HomeEvent.UpdateExpandedItem -> {
                state.copy(
                    expandedItems = state.expandedItems.filter {
                        it.first != event.expandedItem.first
                    }.plus(event.expandedItem)
                ) to null
            }
            is HomeEvent.UpdateLastReceipts -> {
                state.copy(lastReceipts = event.lastReceipts) to null
            }
            is HomeEvent.UpdateMostSale -> {
                state.copy(mostSale = event.mostSale) to null
            }
        }
    }
}