package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.ui.shared.stringToFloat
import com.bruno13palhano.ui.shared.stringToInt
import com.bruno13palhano.ui.shared.stringToLong
import javax.inject.Inject

/**
 * An wrapper for Receipt input fields.
 */
internal class ReceiptFields @Inject constructor() {
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

    fun updateProductName(productName: String) {
        this.productName = productName
    }

    fun updateRequestNumber(requestNumber: String) {
        this.requestNumber = requestNumber
    }

    fun updateRequestDate(requestDate: Long) {
        this.requestDate = requestDate
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

    fun isReceiptValid(): Boolean {
        return requestNumber.isNotBlank() && customerName.isNotBlank() && quantity.isNotBlank() &&
                naturaPrice.isNotBlank() && amazonPrice.isNotBlank() && paymentOption.isNotBlank()
    }

    /**
     * Convert the input fields to a [Receipt] object.
     *
     * @param id The id of the [Receipt].
     *
     * @param product The [Product] object.
     */
    fun toReceipt(
        id: Long = 0L,
        product: Product
    ): Receipt {
        return Receipt(
            id = id,
            product = product,
            requestNumber = stringToLong(requestNumber),
            requestDate = requestDate,
            customerName = customerName,
            quantity = stringToInt(quantity),
            naturaPrice = stringToFloat(naturaPrice),
            amazonPrice = stringToFloat(amazonPrice),
            paymentOption = paymentOption,
            canceled = false,
            observations = observations
        )
    }

    /**
     * Set the input fields of the [Receipt].
     *
     * @param receipt The [Receipt] object.
     */
    fun setFields(receipt: Receipt) {
        updateProductName(productName = receipt.product.name)
        updateRequestNumber(requestNumber = receipt.requestNumber.toString())
        updateRequestDate(requestDate = receipt.requestDate)
        updateCustomerName(customerName = receipt.customerName)
        updateQuantity(quantity = receipt.quantity.toString())
        updateNaturaPrice(naturaPrice = receipt.naturaPrice.toString())
        updateAmazonPrice(amazonPrice = receipt.amazonPrice.toString())
        updatePaymentOption(paymentOption = receipt.paymentOption)
        updateObservations(observations = receipt.observations)
    }
}