package com.bruno13palhano.home.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ReceiptRep
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.home.ui.shared.HomeEffect
import com.bruno13palhano.home.ui.shared.HomeEvent
import com.bruno13palhano.ui.shared.BaseViewModel
import com.bruno13palhano.home.ui.shared.HomeReducer
import com.bruno13palhano.home.ui.shared.HomeState
import com.bruno13palhano.home.ui.shared.MostSaleItem
import com.bruno13palhano.home.ui.shared.Profit
import com.bruno13palhano.home.ui.shared.ReceiptItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    @ReceiptRep private val receiptRepository: ReceiptRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState.INITIAL_STATE,
    reducer = HomeReducer()
) {
    fun getProfit() {
        viewModelScope.launch {
            receiptRepository
                .getAll()
                .collect {
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

                    sendEvent(
                        HomeEvent.UpdateProfit(
                            profit = Profit(
                                profit = profitAmount,
                                amazonProfit = amazonProfit,
                                naturaProfit = naturaProfit
                            )
                        )
                    )

                    if (it.isNotEmpty()) {
                        sendEvent(HomeEvent.UpdateReceiptsVisibility(visible = true))
                    }
                }
        }
    }

    fun getLastReceipts() {
        viewModelScope.launch {
            receiptRepository.getLastReceipts(limit = 5)
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
                .collect {
                    sendEvent(HomeEvent.UpdateLastReceipts(lastReceipts = it))
                }
        }
    }

    fun getMostSale() {
        viewModelScope.launch {
            receiptRepository.getLastReceipts(limit = 5)
                .map {
                    it.map { receipt ->
                        MostSaleItem(
                            id = receipt.id,
                            productName = receipt.product.name,
                            unitsSold = 1
                        )
                    }
                }
                .collect {
                    sendEvent(HomeEvent.UpdateMostSale(mostSale = it))
                }
        }
    }

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