package com.bruno13palhano.receipt.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.receipt.ui.shared.ReceiptsEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptsEvent
import com.bruno13palhano.receipt.ui.shared.ReceiptsReducer
import com.bruno13palhano.receipt.ui.shared.ReceiptsState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
) : BaseViewModel<ReceiptsState, ReceiptsEvent, ReceiptsEffect>(
    initialState = ReceiptsState.INITIAL_STATE,
    reducer = ReceiptsReducer()
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
}