package com.bruno13palhano.model

data class Receipt(
    val id: Long,
    val product: Product,
    val requestNumber: Long,
    val requestDate: Long,
    val customerName: String,
    val quantity: Int,
    val naturaPrice: Float,
    val amazonPrice: Float,
    val paymentOption: String,
    val canceled: Boolean,
    val observations: String
)
