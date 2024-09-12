package com.bruno13palhano.receipt.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    @ProductRep private val productRepository: ProductRepository
) : BaseViewModel<ReceiptState, ReceiptEvent, ReceiptEffect>(
    initialState = ReceiptState.INITIAL_STATE,
    reducer = ReceiptReducer()
) {
    private var product = MutableStateFlow(Product(id = 0L, name = "", naturaCode = ""))

    var productName by mutableStateOf("")
        private set
    var requestNumber by mutableStateOf("")
        private set
    var requestDate by mutableLongStateOf(0L)
        private set
    var customerName by mutableStateOf("")
        private set
    var quantity by mutableStateOf("")
        private set
    var naturaPrice by mutableStateOf("")
        private set
    var amazonPrice by mutableStateOf("")
        private set
    var paymentOption by mutableStateOf("")
        private set
    var observations by mutableStateOf("")
        private set

    fun updateRequestNumber(requestNumber: String) {
        this.requestNumber = requestNumber
    }

    fun updateRequestDate(requestDate: Long) {
        this@ReceiptViewModel.requestDate = requestDate
    }

    fun updateCustomerName(customerName: String) {
        this.customerName = customerName
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateNaturaPrice(naturaPrice: String) {
        this.naturaPrice = naturaPrice
    }

    fun updateAmazonPrice(amazonPrice: String) {
        this.amazonPrice = amazonPrice
    }

    fun updatePaymentOption(paymentOption: String) {
        this.paymentOption = paymentOption
    }

    fun updateObservations(observations: String) {
        this.observations = observations
    }

    fun getProduct(productId: Long) {
        sendEvent(event = ReceiptEvent.NewReceipt)

        viewModelScope.launch {
            productRepository.get(id = productId)
                .catch { it.printStackTrace() }
                .collect {
                    sendEvent(event = ReceiptEvent.UpdateCurrentProduct(product = it))
                    productName = it.name
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
        val isValid = requestNumber.isNotBlank() &&
                customerName.isNotBlank() &&
                quantity.isNotBlank() &&
                naturaPrice.isNotBlank() &&
                amazonPrice.isNotBlank() &&
                paymentOption.isNotBlank()

        sendEvent(event = ReceiptEvent.UpdateHasInvalidField(hasInvalidField = !isValid))

        return isValid
    }

    private fun mapPropertiesToReceipt(id: Long = 0L, canceled: Boolean = false): Receipt {
        return Receipt(
            id = id,
            product = product.value,
            requestNumber = stringToLong(requestNumber),
            requestDate = requestDate,
            customerName = customerName,
            quantity = stringToInt(quantity),
            naturaPrice = stringToFloat(naturaPrice),
            amazonPrice = stringToFloat(amazonPrice),
            paymentOption = paymentOption,
            canceled = canceled,
            observations = observations
        )
    }

    private fun setReceiptProperties(receipt: Receipt) {
        product.value = receipt.product
        productName = receipt.product.name
        requestNumber = receipt.requestNumber.toString()
        requestDate = receipt.requestDate
        customerName = receipt.customerName
        quantity = receipt.quantity.toString()
        naturaPrice = receipt.naturaPrice.toString()
        amazonPrice = receipt.amazonPrice.toString()
        paymentOption = receipt.paymentOption
        observations = receipt.observations
    }
}