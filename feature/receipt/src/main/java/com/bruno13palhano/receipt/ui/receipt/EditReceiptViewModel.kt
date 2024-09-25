package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class EditReceiptViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    val receiptFields: ReceiptFields = ReceiptFields()
) : BaseViewModel<EditReceiptState, EditReceiptAction, EditReceiptEvent, EditReceiptEffect>(
    actionProcessor = EditReceiptActionProcessor()
) {
    @Composable
    override fun states(events: Flow<EditReceiptEvent>): EditReceiptState {
        return editReceiptPresenter(
            receiptFields = receiptFields,
            receiptRepository = receiptRepository,
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: EditReceiptAction) {
        sendEvent(event = actionProcessor.processAction(action))
    }
}