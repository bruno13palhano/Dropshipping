package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository
) : BaseViewModel<ReceiptsState, ReceiptsAction, ReceiptsEvent, ReceiptsEffect>(
    actionProcessor = ReceiptsActionProcessor()
) {
    @Composable
    override fun states(events: Flow<ReceiptsEvent>): ReceiptsState {
        return receiptsPresenter(
            receiptRepository = receiptRepository,
            scope = viewModelScope,
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: ReceiptsAction) {
        sendEvent(event = actionProcessor.processAction(action))
    }
}