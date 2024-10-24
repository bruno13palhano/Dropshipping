package com.bruno13palhano.home.ui.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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

    HandleEvents(events = events, state = state, reducer = reducer)

    GetProfit(profit = profit, sendEvent = sendEvent)

    GetLastReceipts(lastReceipts = lastReceipts, sendEvent = sendEvent)

    GetMostSales(mostSale = mostSale, sendEvent = sendEvent)

    return state.value
}

@Composable
private fun HandleEvents(
    events: Flow<HomeEvent>,
    state: MutableState<HomeState>,
    reducer: Reducer<HomeState, HomeEvent, HomeEffect>
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(previousState = state.value, event = event).let {
                state.value = it.first
            }
        }
    }
}

@Composable
private fun GetProfit(profit: Flow<Profit>, sendEvent: (event: HomeEvent) -> Unit) {
    LaunchedEffect(Unit) {
        profit.collect { sendEvent(HomeEvent.UpdateProfit(profit = it)) }
    }
}

@Composable
private fun GetLastReceipts(
    lastReceipts: Flow<List<ReceiptItem>>,
    sendEvent: (event: HomeEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        lastReceipts.collect { sendEvent(HomeEvent.UpdateLastReceipts(receipts = it)) }
    }
}

@Composable
private fun GetMostSales(
    mostSale: Flow<List<MostSaleItem>>,
    sendEvent: (event: HomeEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        mostSale.collect { sendEvent(HomeEvent.UpdateMostSale(mostSale = it)) }
    }
}