package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class ReceiptViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    @ProductRep private val productRepository: ProductRepository,
    val receiptFields: ReceiptFields = ReceiptFields()
) : BaseViewModel<EditReceiptState, EditReceiptAction, EditReceiptEvent, EditReceiptEffect>(
    actionProcessor = ReceiptActionProcessor()
) {
    @Composable
    override fun states(events: Flow<EditReceiptEvent>): EditReceiptState {
        return editReceiptPresenter(
            receiptFields = receiptFields,
            productRepository = productRepository,
            receiptRepository = receiptRepository,
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: EditReceiptAction) {
        sendEvent(event = actionProcessor.processAction(action))
    }
}