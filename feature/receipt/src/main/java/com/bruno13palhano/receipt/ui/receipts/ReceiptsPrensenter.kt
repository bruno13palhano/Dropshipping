package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.bruno13palhano.data.repository.ReceiptRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
internal fun receiptsPresenter(
    receiptRepository: ReceiptRepository,
    scope: CoroutineScope,
    events: Flow<ReceiptsEvent>,
    sendEffect: (ReceiptsEffect) -> Unit,
): ReceiptsState {
    var editReceipt by remember { mutableStateOf(false) }
    var searchProduct by remember { mutableStateOf(false) }
    val receipts = receiptRepository.pagingReceipts()
        .cachedIn(scope)
        .collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ReceiptsEvent.EditReceipt -> {
                    editReceipt = event.editReceipt
                    sendEffect(ReceiptsEffect.NavigateToEditReceipt(event.id))
                }

                is ReceiptsEvent.SearchProduct -> {
                    searchProduct = event.searching
                    sendEffect(ReceiptsEffect.NavigateToSearchProduct)
                }
            }
        }
    }

    return ReceiptsState(
        editReceipt = editReceipt,
        searchProduct = searchProduct,
        receipts = receipts
    )
}