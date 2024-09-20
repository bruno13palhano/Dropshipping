package com.bruno13palhano.home.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import com.bruno13palhano.ui.shared.Reducer
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
    data class UpdateProfitVisibility(val visible: Boolean) : HomeEvent
    data class UpdateReceiptsVisibility(val visible: Boolean) : HomeEvent
    data class UpdateExpandedItem(val id: Long, val expanded: Boolean) : HomeEvent
}

@Immutable
internal sealed interface HomeEffect : ViewEffect