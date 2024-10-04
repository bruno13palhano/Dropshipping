package com.bruno13palhano.home.ui.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow

@Composable
internal fun homePresenter(
    reducer: Reducer<HomeState, HomeEvent, HomeEffect>,
    profit: Flow<Profit>,
    lastReceipts: Flow<List<ReceiptItem>>,
    mostSale: Flow<List<MostSaleItem>>,
    sendEvent: (event: HomeEvent) -> Unit,
    events: Flow<HomeEvent>
): HomeState {
    val state = remember { mutableStateOf(HomeState.INITIAL_STATE) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(event = event, previousState = state.value).let {
                state.value = it.first
            }
        }
    }

    LaunchedEffect(Unit) {
        getProfit(profit = profit, sendEvent = sendEvent)
    }

    LaunchedEffect(Unit) {
        getLastReceipts(lastReceipts = lastReceipts, sendEvent = sendEvent)
    }

    LaunchedEffect(Unit) {
        getMostSales(mostSale = mostSale, sendEvent = sendEvent)
    }

    return state.value
}

private suspend fun getProfit(
    profit: Flow<Profit>,
    sendEvent: (event: HomeEvent) -> Unit
) {
    profit.collect { sendEvent(HomeEvent.UpdateProfit(profit = it)) }
}

private suspend fun getLastReceipts(
    lastReceipts: Flow<List<ReceiptItem>>,
    sendEvent: (event: HomeEvent) -> Unit
) {
    lastReceipts.collect { sendEvent(HomeEvent.UpdateLastReceipts(receipts = it)) }
}

private suspend fun getMostSales(
    mostSale: Flow<List<MostSaleItem>>,
    sendEvent: (event: HomeEvent) -> Unit
) {
    mostSale.collect { sendEvent(HomeEvent.UpdateMostSale(mostSale = it)) }
}