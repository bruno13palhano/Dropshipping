package com.bruno13palhano.data.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Receipt

@Entity
internal data class ReceiptInternal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val product: ProductInternal,
    val requestNumber: Long,
    val requestDate: Long,
    val quantity: Int,
    val customerName: String,
    val naturaPrice: Float,
    val amazonPrice: Float,
    val amazonTax: Float,
    val naturaPercentageGain: Float,
    val taxes: Float,
    val extras: Float,
    val paymentOption: String,
    val canceled: Boolean,
    val observations: String
)

internal fun ReceiptInternal.asExternal() = Receipt(
    id = id,
    product = product.asExternal(),
    requestNumber = requestNumber,
    requestDate = requestDate,
    quantity = quantity,
    customerName = customerName,
    naturaPrice = naturaPrice,
    amazonPrice = amazonPrice,
    amazonTax = amazonTax,
    naturaPercentageGain = naturaPercentageGain,
    taxes = taxes,
    extras = extras,
    paymentOption = paymentOption,
    canceled = canceled,
    observations = observations
)

internal fun Receipt.asInternal() = ReceiptInternal(
    id = id,
    product = product.asInternal(),
    requestNumber = requestNumber,
    requestDate = requestDate,
    quantity = quantity,
    customerName = customerName,
    naturaPrice = naturaPrice,
    amazonPrice = amazonPrice,
    amazonTax = amazonTax,
    naturaPercentageGain = naturaPercentageGain,
    taxes = taxes,
    extras = extras,
    paymentOption = paymentOption,
    canceled = canceled,
    observations = observations
)