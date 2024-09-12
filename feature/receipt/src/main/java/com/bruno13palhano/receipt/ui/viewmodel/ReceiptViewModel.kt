package com.bruno13palhano.receipt.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.receipt.ui.shared.ReceiptEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptEvent
import com.bruno13palhano.receipt.ui.shared.ReceiptReducer
import com.bruno13palhano.receipt.ui.shared.ReceiptState
import com.bruno13palhano.ui.shared.BaseViewModel
import com.bruno13palhano.ui.shared.stringToFloat
import com.bruno13palhano.ui.shared.stringToInt
import com.bruno13palhano.ui.shared.stringToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    @ProductRep private val productRepository: ProductRepository,
    val receiptInput: ReceiptInput = ReceiptInput()
) : BaseViewModel<ReceiptState, ReceiptEvent, ReceiptEffect>(
    initialState = ReceiptState.INITIAL_STATE,
    reducer = ReceiptReducer()
) {
    private var product = MutableStateFlow(Product(id = 0L, name = "", naturaCode = ""))

    fun getProduct(productId: Long) {
        sendEvent(event = ReceiptEvent.NewReceipt)

        viewModelScope.launch {
            productRepository.get(id = productId)
                .catch { it.printStackTrace() }
                .collect {
                    sendEvent(event = ReceiptEvent.UpdateCurrentProduct(product = it))
                    receiptInput.updateProductName(productName = it.name)
                    product.value = it
                }
        }
    }

    fun getReceipt(id: Long) {
        sendEvent(event = ReceiptEvent.EditReceipt)

        viewModelScope.launch {
            receiptRepository.get(id = id)
                .catch { it.printStackTrace() }
                .collect {
                    sendEvent(event = ReceiptEvent.UpdateCurrentReceipt(receipt = it))
                    setReceiptProperties(receipt = it)
                }
        }
    }

    fun saveReceipt(id: Long) {
        if (!isReceiptValid()) return

        if (id == 0L) {
            sendEvent(event = ReceiptEvent.IsAdding)
        } else {
            sendEvent(event = ReceiptEvent.IsEditing(id = id))
        }
    }

    fun insertReceipt() {
        viewModelScope.launch {
            receiptRepository.insert(data = mapPropertiesToReceipt())
            sendEvent(event = ReceiptEvent.OnAddReceiptSuccessfully)
        }
    }

    fun updateReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.update(data = mapPropertiesToReceipt(id = id))
            sendEvent(event = ReceiptEvent.OnUpdateReceiptSuccessfully)
        }
    }

    fun deleteReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.delete(id = id)
            sendEvent(event = ReceiptEvent.OnDeleteReceiptSuccessfully)
        }
    }

    fun cancelReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.update(data = mapPropertiesToReceipt(id = id, canceled = true))
            sendEvent(event = ReceiptEvent.OnCancelReceiptSuccessfully)
        }
    }

    private fun isReceiptValid(): Boolean {
        val isValid = receiptInput.requestNumber.isNotBlank() &&
                receiptInput.customerName.isNotBlank() &&
                receiptInput.quantity.isNotBlank() &&
                receiptInput.naturaPrice.isNotBlank() &&
                receiptInput.amazonPrice.isNotBlank() &&
                receiptInput.paymentOption.isNotBlank()

        sendEvent(event = ReceiptEvent.UpdateHasInvalidField(hasInvalidField = !isValid))

        return isValid
    }

    private fun mapPropertiesToReceipt(id: Long = 0L, canceled: Boolean = false): Receipt {
        return Receipt(
            id = id,
            product = product.value,
            requestNumber = stringToLong(receiptInput.requestNumber),
            requestDate = receiptInput.requestDate,
            customerName = receiptInput.customerName,
            quantity = stringToInt(receiptInput.quantity),
            naturaPrice = stringToFloat(receiptInput.naturaPrice),
            amazonPrice = stringToFloat(receiptInput.amazonPrice),
            paymentOption = receiptInput.paymentOption,
            canceled = canceled,
            observations = receiptInput.observations
        )
    }

    private fun setReceiptProperties(receipt: Receipt) {
        product.value = receipt.product
        receiptInput.updateProductName(productName = receipt.product.name)
        receiptInput.updateRequestNumber(requestNumber = receipt.requestNumber.toString())
        receiptInput.updateRequestDate(requestDate = receipt.requestDate)
        receiptInput.updateCustomerName(customerName = receipt.customerName)
        receiptInput.updateQuantity(quantity = receipt.quantity.toString())
        receiptInput.updateNaturaPrice(naturaPrice = receipt.naturaPrice.toString())
        receiptInput.updateAmazonPrice(amazonPrice = receipt.amazonPrice.toString())
        receiptInput.updatePaymentOption(paymentOption = receipt.paymentOption)
        receiptInput.updateObservations(observations = receipt.observations)
    }
}