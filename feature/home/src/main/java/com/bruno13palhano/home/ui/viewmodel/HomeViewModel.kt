package com.bruno13palhano.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
) : ViewModel() {
    val profit = receiptRepository
        .getAll()
        .map {
            var amazonProfit = .0f
            var naturaProfit = .0f
            var profitAmount = .0f

            it.map { receipt ->
                amazonProfit += calculateAmazonProfit(amazonPrice = receipt.amazonPrice)
                naturaProfit += calculateNaturaProfit(naturaPrice = receipt.naturaPrice)
                profitAmount += calculateProfit(
                    amazonPrice = receipt.amazonPrice,
                    naturaPrice = receipt.naturaPrice
                )
            }

            Profit(
                profit = profitAmount,
                amazonProfit = amazonProfit,
                naturaProfit = naturaProfit
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = Profit(0f,0f,0f)
        )

    val lasReceipts = receiptRepository.getLastReceipts(limit = 5)
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
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val mostSale = receiptRepository.getLastReceipts(limit = 5)
        .map {
            it.map { receipt ->
                MostSaleItem(
                    id = receipt.id,
                    productName = receipt.product.name,
                    unitsSold = 1
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun calculateProfit(
        amazonPrice: Float,
        naturaPrice: Float,
        amazonTax: Float = .13f,
        naturaPercentageGain: Float = .20f,
        taxes: Float = 0f,
        extras: Float = 0f
    ): Float {
        return calculateAmazonProfit(amazonPrice = amazonPrice, amazonTax = amazonTax) +
                calculateNaturaProfit(naturaPrice = naturaPrice, naturaPercentageGain = naturaPercentageGain) -
                calculateProductCost(naturaPrice = naturaPrice, taxes = taxes, extras = extras)
    }

    private fun calculateProductCost(naturaPrice: Float, taxes: Float, extras: Float = 0f): Float {
        return (taxes * naturaPrice) + naturaPrice + extras
    }

    private fun calculateAmazonProfit(amazonPrice: Float, amazonTax: Float = .13f): Float {
        return (1 - amazonTax) * amazonPrice
    }

    private fun calculateNaturaProfit(
        naturaPercentageGain: Float = .20f,
        naturaPrice: Float
    ): Float {
        return naturaPercentageGain * naturaPrice
    }
}

data class ReceiptItem(
    val id: Long,
    val customerName: String,
    val productName: String,
    val amazonPrice: Float,
    val requestDate: Long,
)

data class MostSaleItem(
    val id: Long,
    val productName: String,
    val unitsSold: Int
)

data class Profit(
    val profit: Float,
    val amazonProfit: Float,
    val naturaProfit: Float,
)