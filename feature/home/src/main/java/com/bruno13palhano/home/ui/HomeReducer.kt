package com.bruno13palhano.home.ui

import com.bruno13palhano.ui.shared.Reducer

internal class HomeReducer : Reducer<HomeState, HomeEvent, HomeEffect> {
    override fun reduce(
        previousState: HomeState,
        event: HomeEvent
    ): Pair<HomeState, HomeEffect?> {
        return when (event) {
            is HomeEvent.ProfitVisibility -> {
                previousState.copy(profitVisible = event.visible) to null
            }

            is HomeEvent.ReceiptsVisibility -> {
                previousState.copy(receiptsVisible = event.visible) to null
            }

            is HomeEvent.ExpandedItem -> {
                previousState.copy(
                    expandedItems = previousState.expandedItems.filter {
                        it.first != event.id
                    }.plus(Pair(event.id, event.expanded))
                ) to null
            }

            is HomeEvent.UpdateLastReceipts -> {
                val receiptsVisible = previousState.lastReceipts.isNotEmpty()

                previousState.copy(
                    lastReceipts = event.receipts,
                    receiptsVisible = receiptsVisible
                ) to null
            }

            is HomeEvent.UpdateMostSale -> {
                previousState.copy(mostSale = event.mostSale) to null
            }

            is HomeEvent.UpdateProfit -> {
                previousState.copy(profit = event.profit) to null
            }
        }
    }
}