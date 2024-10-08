package com.bruno13palhano.receipt.ui.receipt.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptAction
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptActionProcessor
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptEffect
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptEvent
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptReducer
import com.bruno13palhano.receipt.ui.receipt.presenter.NewReceiptState
import com.bruno13palhano.receipt.ui.receipt.presenter.ReceiptFields
import com.bruno13palhano.receipt.ui.receipt.presenter.newReceiptPresenter
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class NewReceiptViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    val receiptFields: ReceiptFields = ReceiptFields()
) : BaseViewModel<NewReceiptState, NewReceiptAction, NewReceiptEvent, NewReceiptEffect> (
    actionProcessor = NewReceiptActionProcessor(),
    reducer = NewReceiptReducer(receiptFields = receiptFields)
) {
    @Composable
    override fun states(events: Flow<NewReceiptEvent>): NewReceiptState {
        return newReceiptPresenter(
            receiptFields = receiptFields,
            productRepository = productRepository,
            receiptRepository = receiptRepository,
            reducer = reducer,
            events = events,
            sendEvent = ::sendEvent,
            sendEffect = ::sendEffect
        )
    }
}