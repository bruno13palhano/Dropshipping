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
) {
    companion object {
        val EMPTY = Receipt(
            id = 0L,
            product = Product.EMPTY,
            requestNumber = 0L,
            requestDate = 0L,
            customerName = "",
            quantity = 0,
            naturaPrice = 0F,
            amazonPrice = 0F,
            paymentOption = "",
            canceled = false,
            observations = ""
        )
    }
}
