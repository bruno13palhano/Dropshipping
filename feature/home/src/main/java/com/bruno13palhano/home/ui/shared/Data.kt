package com.bruno13palhano.home.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class HomeState(
    val profitVisible: Boolean,
    val profit: Profit,
    val receiptsVisible: Boolean,
    val lastReceipts: List<ReceiptItem>,
    val mostSale: List<MostSaleItem>,
    val expandedItems: List<Pair<Long, Boolean>>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE =  HomeState(
            profitVisible = false,
            receiptsVisible = false,
            profit = Profit(0f,0f,0f),
            lastReceipts = emptyList(),
            mostSale = emptyList(),
            expandedItems = emptyList()
        )
    }
}

@Immutable
internal sealed interface HomeEvent : Reducer.ViewEvent {
    data class UpdateProfitVisibility(val visible: Boolean) : HomeEvent
    data class UpdateProfit(val profit: Profit) : HomeEvent
    data class UpdateReceiptsVisibility(val visible: Boolean) : HomeEvent
    data class UpdateLastReceipts(val lastReceipts: List<ReceiptItem>) : HomeEvent
    data class UpdateMostSale(val mostSale: List<MostSaleItem>) : HomeEvent
    data class UpdateExpandedItem(val id: Long, val expanded: Boolean) : HomeEvent
}

@Immutable
internal sealed interface HomeEffect : Reducer.ViewEffect