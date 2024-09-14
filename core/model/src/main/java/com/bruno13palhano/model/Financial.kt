package com.bruno13palhano.model

data class Financial(
    val amazonPrice: Float,
    val naturaPrice: Float,
    val amazonTax: Float = .13f,
    val naturaPercentageGain: Float = .20f,
    val taxes: Float = 0f,
    val extras: Float = 0f
) {
    fun calculateProfit() =
        calculateAmazonProfit() + calculateNaturaProfit() - calculateProductCost()

    fun calculateProductCost() = (taxes * naturaPrice) + naturaPrice + extras

    fun calculateAmazonProfit() = (1 - amazonTax) * amazonPrice

    fun calculateNaturaProfit() = naturaPercentageGain * naturaPrice
}