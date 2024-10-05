package com.bruno13palhano.receipt.ui.receipts.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer
import kotlinx.coroutines.flow.Flow

@Composable
internal fun receiptsPresenter(
    receipts: Flow<List<Receipt>>,
    events: Flow<ReceiptsEvent>,
    reducer: Reducer<ReceiptsState, ReceiptsEvent, ReceiptsEffect>,
    sendEvent: (event: ReceiptsEvent) -> Unit,
    sendEffect: (ReceiptsEffect) -> Unit,
): ReceiptsState {
    val state = remember { mutableStateOf(ReceiptsState.INITIAL_STATE) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            reducer.reduce(previousState = state.value, event = event).let {
                state.value = it.first
                it.second?.let{ effect -> sendEffect(effect) }
            }
        }
    }

    LaunchedEffect(Unit) { getReceipts(receipts = receipts, sendEvent = sendEvent) }

    return state.value
}

private suspend fun getReceipts(
    receipts: Flow<List<Receipt>>,
    sendEvent: (event: ReceiptsEvent) -> Unit
) {
    receipts.collect { sendEvent(ReceiptsEvent.UpdateReceipts(receipts = it)) }
}