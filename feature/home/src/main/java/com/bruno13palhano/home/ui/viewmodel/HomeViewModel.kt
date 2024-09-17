package com.bruno13palhano.home.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.home.ui.shared.HomeEffect
import com.bruno13palhano.home.ui.shared.HomeEvent
import com.bruno13palhano.ui.shared.BaseViewModel
import com.bruno13palhano.home.ui.shared.HomeState
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    @Composable
    override fun states(events: Flow<HomeEvent>, effects: Flow<HomeEffect>?): HomeState {
        return homePresenter(useCase = homeUseCase)
    }

    @Composable
    fun homePresenter(useCase: HomeUseCase): HomeState {
        var profit by remember { mutableStateOf(Profit(0f, 0f, 0f)) }
        var lastReceipts by remember { mutableStateOf(emptyList<ReceiptItem>()) }
        var mostSale by remember { mutableStateOf(emptyList<MostSaleItem>()) }
        var profitVisible by remember { mutableStateOf(false) }
        var receiptsVisible by remember { mutableStateOf(false) }
        var expandedItems by remember { mutableStateOf(emptyList<Pair<Long, Boolean>>()) }

        LaunchedEffect(key1 = Unit) {
            useCase.getProfit().collect {
                profit = it
            }
        }

        LaunchedEffect(key1 = Unit) {
            useCase.getLastReceipts(limit = 5).collect {
                lastReceipts = it

                if (it.isNotEmpty()) {
                    receiptsVisible = true
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            useCase.getMostSale(limit = 5).collect {
                mostSale = it
            }
        }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is HomeEvent.UpdateExpandedItem -> {
                        expandedItems = expandedItems.filter {
                            it.first != event.id
                        }.plus(Pair(event.id, event.expanded))
                    }

                    is HomeEvent.UpdateProfitVisibility -> {
                        profitVisible = event.visible
                    }

                    is HomeEvent.UpdateReceiptsVisibility -> {
                        receiptsVisible = event.visible
                    }
                }
            }
        }

        return HomeState(
            profitVisible = profitVisible,
            receiptsVisible = receiptsVisible,
            profit = profit,
            lastReceipts = lastReceipts,
            mostSale = mostSale,
            expandedItems = expandedItems
        )
    }
}