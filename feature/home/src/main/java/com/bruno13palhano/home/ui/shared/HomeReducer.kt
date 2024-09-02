package com.bruno13palhano.home.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class HomeReducer : Reducer<HomeState, HomeEvent, HomeEffect> {
    override fun reduce(
        previousState: HomeState,
        event: HomeEvent
    ): Pair<HomeState, HomeEffect?> {
        return when (event) {
            is HomeEvent.UpdateProfitVisibility -> {
                previousState.copy(profitVisible = event.visible) to null
            }
            is HomeEvent.UpdateProfit -> {
                previousState.copy(profit = event.profit) to null
            }
            is HomeEvent.UpdateReceiptsVisibility -> {
                previousState.copy(receiptsVisible = event.visible) to null
            }
            is HomeEvent.UpdateExpandedItem -> {
                previousState.copy(
                    expandedItems = previousState.expandedItems.filter {
                        it.first != event.expandedItem.first
                    }.plus(event.expandedItem)
                ) to null
            }
            is HomeEvent.UpdateLastReceipts -> {
                previousState.copy(lastReceipts = event.lastReceipts) to null
            }
            is HomeEvent.UpdateMostSale -> {
                previousState.copy(mostSale = event.mostSale) to null
            }
        }
    }
}