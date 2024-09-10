package com.bruno13palhano.receipt.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class ReceiptState(
    val editReceipt: Boolean,
    val newReceipt: Boolean,
    val editing: Boolean,
    val adding: Boolean,
    val hasInvalidField: Boolean,
    val deleteReceipt: Boolean,
    val canceled: Boolean,
    val receipt: Receipt,
    val navigateBack: Boolean
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ReceiptState(
            editReceipt = false,
            newReceipt = false,
            editing = false,
            adding = false,
            hasInvalidField = false,
            deleteReceipt = false,
            canceled = false,
            receipt = Receipt(
                id = 0L,
                product = Product(id = 0L, name = "", naturaCode = ""),
                requestNumber = 0L,
                requestDate = 0L,
                customerName = "",
                quantity = 0,
                naturaPrice = 0F,
                amazonPrice = 0F,
                paymentOption = "",
                canceled = false,
                observations = ""
            ),
            navigateBack = false
        )
    }
}

@Immutable
internal sealed interface ReceiptEvent : Reducer.ViewEvent {
    data object EditReceipt : ReceiptEvent
    data object NewReceipt : ReceiptEvent
    data object IsEditing : ReceiptEvent
    data object IsAdding : ReceiptEvent
    data class UpdateHasInvalidField(val hasInvalidField: Boolean) : ReceiptEvent
    data class UpdateCanceled(val canceled: Boolean) : ReceiptEvent
    data class UpdateProduct(val product: Product) : ReceiptEvent
    data class UpdateReceipt(val receipt: Receipt) : ReceiptEvent
    data class UpdateDeleteReceipt(val deleteReceipt: Boolean, val id: Long) : ReceiptEvent
    data object OnDeleteReceiptSuccessfully : ReceiptEvent
    data object OnNavigateBack : ReceiptEvent
}

@Immutable
internal sealed interface ReceiptEffect : Reducer.ViewEffect {
    data class DeleteReceipt(val id: Long) : ReceiptEffect
    data object NavigateBack : ReceiptEffect
}