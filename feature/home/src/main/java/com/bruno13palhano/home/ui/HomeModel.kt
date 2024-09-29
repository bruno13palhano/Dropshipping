package com.bruno13palhano.home.ui

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class HomeState(
    val profitVisible: Boolean,
    val profit: Profit,
    val receiptsVisible: Boolean,
    val lastReceipts: List<ReceiptItem>,
    val mostSale: List<MostSaleItem>,
    val expandedItems: List<Pair<Long, Boolean>>
) : ViewState {
    companion object {
        val INITIAL_STATE = HomeState(
            profitVisible = false,
            profit = Profit(0f, 0f, 0f),
            receiptsVisible = false,
            lastReceipts = emptyList(),
            mostSale = emptyList(),
            expandedItems = emptyList()
        )
    }
}

@Immutable
internal sealed interface HomeEvent : ViewEvent {
    data class ProfitVisibility(val visible: Boolean) : HomeEvent
    data class ReceiptsVisibility(val visible: Boolean) : HomeEvent
    data class ExpandedItem(val id: Long, val expanded: Boolean) : HomeEvent
    data class UpdateProfit(val profit: Profit) : HomeEvent
    data class UpdateLastReceipts(val receipts: List<ReceiptItem>) : HomeEvent
    data class UpdateMostSale(val mostSale: List<MostSaleItem>) : HomeEvent
}

@Immutable
internal sealed interface HomeEffect : ViewEffect

@Immutable
internal sealed interface HomeAction : ViewAction {
    data class OnProfitVisibilityChanged(val visible: Boolean) : HomeAction
    data class OnReceiptsVisibilityChanged(val visible: Boolean) : HomeAction
    data class OnExpandedItemChanged(val id: Long, val expanded: Boolean) : HomeAction
}