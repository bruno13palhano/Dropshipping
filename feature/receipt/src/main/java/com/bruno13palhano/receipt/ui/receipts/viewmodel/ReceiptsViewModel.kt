package com.bruno13palhano.receipt.ui.receipts.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsAction
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsActionProcessor
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsEffect
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsEvent
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsReducer
import com.bruno13palhano.receipt.ui.receipts.presenter.ReceiptsState
import com.bruno13palhano.receipt.ui.receipts.presenter.receiptsPresenter
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class ReceiptsViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository
) : BaseViewModel<ReceiptsState, ReceiptsAction, ReceiptsEvent, ReceiptsEffect>(
    actionProcessor = ReceiptsActionProcessor(),
    reducer = ReceiptsReducer()
) {
    @Composable
    override fun states(events: Flow<ReceiptsEvent>): ReceiptsState {
        return receiptsPresenter(
            receipts = receiptRepository.getAll(),
            events = events,
            reducer = reducer,
            sendEvent = ::sendEvent,
            sendEffect = ::sendEffect
        )
    }
}