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
    val receiptFields: ReceiptFields = ReceiptFields()
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
                    receiptFields.updateProductName(productName = it.name)
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
        val isValid = receiptFields.requestNumber.isNotBlank() &&
                receiptFields.customerName.isNotBlank() &&
                receiptFields.quantity.isNotBlank() &&
                receiptFields.naturaPrice.isNotBlank() &&
                receiptFields.amazonPrice.isNotBlank() &&
                receiptFields.paymentOption.isNotBlank()

        sendEvent(event = ReceiptEvent.UpdateHasInvalidField(hasInvalidField = !isValid))

        return isValid
    }

    private fun mapPropertiesToReceipt(id: Long = 0L, canceled: Boolean = false): Receipt {
        return Receipt(
            id = id,
            product = product.value,
            requestNumber = stringToLong(receiptFields.requestNumber),
            requestDate = receiptFields.requestDate,
            customerName = receiptFields.customerName,
            quantity = stringToInt(receiptFields.quantity),
            naturaPrice = stringToFloat(receiptFields.naturaPrice),
            amazonPrice = stringToFloat(receiptFields.amazonPrice),
            paymentOption = receiptFields.paymentOption,
            canceled = canceled,
            observations = receiptFields.observations
        )
    }

    private fun setReceiptProperties(receipt: Receipt) {
        product.value = receipt.product
        receiptFields.updateProductName(productName = receipt.product.name)
        receiptFields.updateRequestNumber(requestNumber = receipt.requestNumber.toString())
        receiptFields.updateRequestDate(requestDate = receipt.requestDate)
        receiptFields.updateCustomerName(customerName = receipt.customerName)
        receiptFields.updateQuantity(quantity = receipt.quantity.toString())
        receiptFields.updateNaturaPrice(naturaPrice = receipt.naturaPrice.toString())
        receiptFields.updateAmazonPrice(amazonPrice = receipt.amazonPrice.toString())
        receiptFields.updatePaymentOption(paymentOption = receipt.paymentOption)
        receiptFields.updateObservations(observations = receipt.observations)
    }
}