package com.bruno13palhano.model

data class ReceiptItem(
    val id: Long,
    val customerName: String,
    val productName: String,
    val amazonPrice: Float,
    val requestDate: Long,
)