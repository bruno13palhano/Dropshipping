package com.bruno13palhano.receipt.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.receipt.ui.receipts.ReceiptsAction
import com.bruno13palhano.receipt.ui.receipts.ReceiptsActionProcessor
import com.bruno13palhano.receipt.ui.receipts.ReceiptsEffect
import com.bruno13palhano.receipt.ui.receipts.ReceiptsEvent
import com.bruno13palhano.receipt.ui.receipts.ReceiptsState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository
) : BaseViewModel<ReceiptsState, ReceiptsAction, ReceiptsEvent, ReceiptsEffect>(
    actionProcessor = ReceiptsActionProcessor()
) {
    fun getReceipts() {
        viewModelScope.launch {
            sendEvent(
                event = ReceiptsEvent.UpdateReceipts(
                    isLoading = true,
                    receipts = receiptRepository.pagingReceipts().cachedIn(viewModelScope)
                )
            )
        }
    }

    @Composable
    override fun states(events: Flow<ReceiptsEvent>): ReceiptsState {
        return receiptsPresenter(receiptRepository = receiptRepository)
    }

    @Composable
    fun receiptsPresenter(receiptRepository: ReceiptRepository): ReceiptsState {
        var receiptsLoading by remember { mutableStateOf(false) }
        var editReceipt by remember { mutableStateOf(false) }
        var searchProduct by remember { mutableStateOf(false) }
        var receipts = receiptRepository.pagingReceipts().cachedIn(viewModelScope)

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is ReceiptsEvent.UpdateReceipts -> {
                        receiptsLoading = event.isLoading
                        receipts = event.receipts
                    }

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
            receiptsLoading = receiptsLoading,
            editReceipt = editReceipt,
            searchProduct = searchProduct,
            receipts = receipts
        )
    }
}