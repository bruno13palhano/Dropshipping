package com.bruno13palhano.home.ui.shared

import androidx.compose.runtime.Immutable
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
    data class UpdateExpandedItem(val expandedItem: Pair<Long, Boolean>) : HomeEvent
}

@Immutable
internal sealed interface HomeEffect : Reducer.ViewEffect

internal data class ReceiptItem(
    val id: Long,
    val customerName: String,
    val productName: String,
    val amazonPrice: Float,
    val requestDate: Long,
)

internal data class MostSaleItem(
    val id: Long,
    val productName: String,
    val unitsSold: Int
)

internal data class Profit(
    val profit: Float,
    val amazonProfit: Float,
    val naturaProfit: Float,
)