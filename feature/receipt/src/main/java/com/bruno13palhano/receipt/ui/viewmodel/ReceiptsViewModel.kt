package com.bruno13palhano.receipt.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.receipt.ui.shared.ReceiptsEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptsEvent
import com.bruno13palhano.receipt.ui.shared.ReceiptsReducer
import com.bruno13palhano.receipt.ui.shared.ReceiptsState
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
) : BaseViewModel<ReceiptsState, ReceiptsEvent, ReceiptsEffect>(
    initialState = ReceiptsState.INITIAL_STATE,
    reducer = ReceiptsReducer()
) {
    fun onEditReceiptClick(id: Long) {
        sendEvent(event = ReceiptsEvent.EditReceipt(editReceipt = true, id = id))
    }

    fun onAddReceiptClick() {
        sendEvent(event = ReceiptsEvent.SearchProduct(searching = true))
    }

    fun onDeleteReceiptClick(id: Long) {
        sendEvent(event = ReceiptsEvent.UpdateDeletingReceipt(isDeleting = true, id = id))
    }

    fun deleteReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.delete(id = id)
            sendEvent(event = ReceiptsEvent.OnDeleteReceiptSuccessfully)
        }
    }

    fun getReceipts() {
        viewModelScope.launch {
            receiptRepository.getAll()
                .map {
                    it.map { receipt ->
                        CommonItem(
                            id = receipt.id,
                            title = receipt.customerName
                        )
                    }
                }
                .collect {
                    sendEvent(
                        event = ReceiptsEvent.UpdateReceipts(
                            isLoading = true,
                            receipts = it
                        )
                    )
                }
        }
    }
}