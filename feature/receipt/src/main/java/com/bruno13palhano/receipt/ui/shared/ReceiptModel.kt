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
            receipt = Receipt.EMPTY,
            navigateBack = false
        )
    }
}

@Immutable
internal sealed interface ReceiptEvent : Reducer.ViewEvent {
    data object EditReceipt : ReceiptEvent
    data object NewReceipt : ReceiptEvent
    data class IsEditing(val id: Long) : ReceiptEvent
    data object IsAdding : ReceiptEvent
    data class UpdateHasInvalidField(val hasInvalidField: Boolean) : ReceiptEvent
    data class CancelReceipt(val id: Long) : ReceiptEvent
    data class UpdateCurrentProduct(val product: Product) : ReceiptEvent
    data class UpdateCurrentReceipt(val receipt: Receipt) : ReceiptEvent
    data class UpdateDeleteReceipt(val id: Long) : ReceiptEvent
    data object OnUpdateReceiptSuccessfully : ReceiptEvent
    data object OnAddReceiptSuccessfully : ReceiptEvent
    data object OnCancelReceiptSuccessfully : ReceiptEvent
    data object OnDeleteReceiptSuccessfully : ReceiptEvent
    data object OnNavigateBack : ReceiptEvent
}

@Immutable
internal sealed interface ReceiptEffect : Reducer.ViewEffect {
    data class UpdateReceipt(val id: Long) : ReceiptEffect
    data object InsertReceipt : ReceiptEffect
    data class DeleteReceipt(val id: Long) : ReceiptEffect
    data class CancelReceipt(val id: Long) : ReceiptEffect
    data object InvalidFieldErrorMessage : ReceiptEffect
    data object NavigateBack : ReceiptEffect
}