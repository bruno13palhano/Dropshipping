package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bruno13palhano.model.Receipt
import kotlinx.coroutines.flow.Flow

@Composable
internal fun receiptsPresenter(
    receipts: Flow<List<Receipt>>,
    events: Flow<ReceiptsEvent>,
    sendEffect: (ReceiptsEffect) -> Unit,
): ReceiptsState {
    val receiptList by receipts.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ReceiptsEvent.EditReceipt -> {
                    sendEffect(ReceiptsEffect.NavigateToEditReceipt(event.id))
                }

                is ReceiptsEvent.SearchProduct -> {
                    sendEffect(ReceiptsEffect.NavigateToSearchProduct)
                }
            }
        }
    }

    return ReceiptsState(receipts = receiptList)
}