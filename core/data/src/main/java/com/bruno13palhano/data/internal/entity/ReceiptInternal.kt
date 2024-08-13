package com.bruno13palhano.data.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt

@Entity
data class ReceiptInternal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long,
    val requestNumber: Long,
    val requestDate: Long,
    val quantity: Int,
    val customerName: String,
    val naturaPrice: Float,
    val amazonPrice: Float,
    val paymentOption: String,
    val canceled: Boolean,
    val observations: String
)

internal fun ReceiptInternal.asExternal(product: Product) = Receipt(
    id = id,
    product = product,
    requestNumber = requestNumber,
    requestDate = requestDate,
    quantity = quantity,
    customerName = customerName,
    naturaPrice = naturaPrice,
    amazonPrice = amazonPrice,
    paymentOption = paymentOption,
    canceled = canceled,
    observations = observations
)

internal fun Receipt.asInternal() = ReceiptInternal(
    id = id,
    productId = product.id,
    requestNumber = requestNumber,
    requestDate = requestDate,
    quantity = quantity,
    customerName = customerName,
    naturaPrice = naturaPrice,
    amazonPrice = amazonPrice,
    paymentOption = paymentOption,
    canceled = canceled,
    observations = observations
)