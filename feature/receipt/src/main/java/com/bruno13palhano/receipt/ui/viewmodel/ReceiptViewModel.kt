package com.bruno13palhano.receipt.ui.viewmodel

import androidx.collection.mutableLongSetOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReceiptViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
    @ProductRep private val productRepository: ProductRepository
): ViewModel() {
    private var id: Long =  0L
    private var product = MutableStateFlow(Product(id = 0L, name = "", naturaCode = ""))
    private var canceled: Boolean = false

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
        viewModelScope.launch {
            productRepository.get(id = productId)
                .catch { it.printStackTrace() }
                .collect { product: Product ->
                    this@ReceiptViewModel.product.update { product }
                    productName = product.name
                }
        }
    }

    fun getReceipt(id: Long) {
        viewModelScope.launch {
            receiptRepository.get(id = id)
                .catch { it.printStackTrace() }
                .collect { receipt: Receipt ->
                    setReceiptProperties(receipt = receipt)
                    productName = receipt.product.name
                }
        }
    }

    fun addReceipt() {
        if (!isReceiptValid()) return

        viewModelScope.launch {
            receiptRepository.insert(data = saveReceipt())
        }
    }

    fun updateReceipt() {
        if (!isReceiptValid()) return

        viewModelScope.launch {
            receiptRepository.update(data = saveReceipt(id = id))
        }
    }

    fun deleteReceipt() {
        viewModelScope.launch {
            receiptRepository.delete(id = id)
        }
    }

    fun cancelReceipt() {
        viewModelScope.launch {
            receiptRepository.update(data = saveReceipt(id = id, canceled = true))
        }
    }

    private fun isReceiptValid(): Boolean {
        return requestNumber.isNotBlank() &&
                customerName.isNotBlank() &&
                quantity.isNotBlank() &&
                naturaPrice.isNotBlank() &&
                amazonPrice.isNotBlank() &&
                paymentOption.isNotBlank()
    }

    private fun saveReceipt(id: Long = 0L, canceled: Boolean = false): Receipt {
        return Receipt(
            id = id,
            product = product.value,
            requestNumber = requestNumber.toLong(),
            requestDate = requestDate.toLong(),
            customerName = customerName,
            quantity = quantity.toInt(),
            naturaPrice = naturaPrice.toFloat(),
            amazonPrice = amazonPrice.toFloat(),
            paymentOption = paymentOption,
            canceled = canceled,
            observations = observations
        )
    }

    private fun setReceiptProperties(receipt: Receipt) {
        id = receipt.id
        requestNumber = receipt.requestNumber.toString()
        requestDate = receipt.requestDate
        customerName = receipt.customerName
        quantity = receipt.quantity.toString()
        naturaPrice = receipt.naturaPrice.toString()
        amazonPrice = receipt.amazonPrice.toString()
        paymentOption = receipt.paymentOption
        observations = receipt.observations
        canceled = receipt.canceled
    }
}