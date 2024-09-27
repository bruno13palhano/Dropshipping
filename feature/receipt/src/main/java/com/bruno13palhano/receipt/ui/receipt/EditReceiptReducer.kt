package com.bruno13palhano.receipt.ui.receipt

import com.bruno13palhano.ui.shared.Reducer

internal class ReceiptReducer : Reducer<EditReceiptState, EditReceiptEvent, EditReceiptEffect> {
    override fun reduce(
        previousState: EditReceiptState,
        event: EditReceiptEvent
    ): Pair<EditReceiptState, EditReceiptEffect?> {
        return when (event) {
            is EditReceiptEvent.SetInitialData -> {
                previousState to null
            }

            is EditReceiptEvent.Cancel -> {
                previousState to null
            }

            is EditReceiptEvent.Delete -> {
                previousState to null
            }

            is EditReceiptEvent.Done -> {
                previousState to null
            }

            is EditReceiptEvent.NavigateBack -> {
                previousState to null
            }
        }
    }
}