package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bruno13palhano.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

@Composable
internal fun receiptsPresenter(
    receiptRepository: ReceiptRepository,
    events: Flow<ReceiptsEvent>,
    sendEffect: (ReceiptsEffect) -> Unit,
): ReceiptsState {
    val receipts by receiptRepository.getAll().collectAsState(initial = emptyList())

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

    return ReceiptsState(receipts = receipts)
}