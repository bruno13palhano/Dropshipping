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
) : ViewState

@Immutable
internal sealed interface HomeEvent : ViewEvent {
    data class ProfitVisibility(val visible: Boolean) : HomeEvent
    data class ReceiptsVisibility(val visible: Boolean) : HomeEvent
    data class ExpandedItem(val id: Long, val expanded: Boolean) : HomeEvent
}

@Immutable
internal sealed interface HomeEffect : ViewEffect

@Immutable
internal sealed interface HomeAction : ViewAction {
    data class OnProfitVisibilityChanged(val visible: Boolean) : HomeAction
    data class OnReceiptsVisibilityChanged(val visible: Boolean) : HomeAction
    data class OnExpandedItemChanged(val id: Long, val expanded: Boolean) : HomeAction
}