package com.bruno13palhano.domain

import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Financial
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val receiptRepository: ReceiptRepository
) {
    fun getProfit(): Flow<Profit> {
        return receiptRepository.getAll()
            .map {
                var amazonProfit = .0f
                var naturaProfit = .0f
                var profitAmount = .0f

                it.map { receipt ->
                    val financial = Financial(
                        amazonPrice = receipt.amazonPrice,
                        naturaPrice = receipt.naturaPrice,
                        amazonTax = receipt.amazonTax,
                        naturaPercentageGain = receipt.naturaPercentageGain,
                        taxes = receipt.taxes,
                        extras = receipt.extras
                    )

                    amazonProfit += financial.calculateAmazonProfit()
                    naturaProfit += financial.calculateNaturaProfit()
                    profitAmount += financial.calculateProfit()
                }

                Profit(
                    profit = profitAmount,
                    amazonProfit = amazonProfit,
                    naturaProfit = naturaProfit
                )
            }
    }

    fun getLastReceipts(limit: Int): Flow<List<ReceiptItem>> {
        return receiptRepository.getLastReceipts(limit = limit)
            .map {
                it.map { receipt ->
                    ReceiptItem(
                        id = receipt.id,
                        customerName = receipt.customerName,
                        productName = receipt.product.name,
                        amazonPrice = receipt.amazonPrice,
                        requestDate = receipt.requestDate
                    )
                }
            }
    }

    fun getMostSale(limit: Int): Flow<List<MostSaleItem>> {
        return receiptRepository.getAll()
            .map { receipts ->
                receipts
                    .groupBy { receipt -> receipt.product.name }
                    .map { item ->
                        val unitsSold = item.value.sumOf { receipt -> receipt.quantity }

                        MostSaleItem(
                            id = item.value[0].product.id,
                            productName = item.key,
                            unitsSold = unitsSold
                        )
                    }
                    .sortedByDescending { it.unitsSold }
                    .take(limit)
            }
    }
}