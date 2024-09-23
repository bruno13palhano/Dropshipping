package com.bruno13palhano.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import kotlinx.coroutines.flow.Flow

@Composable
internal fun homePresenter(
    useCase: HomeUseCase,
    events: Flow<HomeEvent>
): HomeState {
    val profit = profitAsState(useCase = useCase)
    val lastReceipts = lastReceiptsAsState(useCase = useCase)
    val mostSale = mostSaleAsState(useCase = useCase)
    var profitVisible by remember { mutableStateOf(false) }
    var receiptsVisible by remember { mutableStateOf(false) }
    var expandedItems by remember { mutableStateOf(emptyList<Pair<Long, Boolean>>()) }

    LaunchedEffect(lastReceipts.value.isNotEmpty()) {
        receiptsVisible = true
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is HomeEvent.ExpandedItem -> {
                    expandedItems = expandedItems.filter {
                        it.first != event.id
                    }.plus(Pair(event.id, event.expanded))
                }

                is HomeEvent.ProfitVisibility -> {
                    profitVisible = event.visible
                }

                is HomeEvent.ReceiptsVisibility -> {
                    receiptsVisible = event.visible
                }
            }
        }
    }

    return HomeState(
        profitVisible = profitVisible,
        receiptsVisible = receiptsVisible,
        profit = profit.value,
        lastReceipts = lastReceipts.value,
        mostSale = mostSale.value,
        expandedItems = expandedItems
    )
}

@Composable
private fun profitAsState(
    useCase: HomeUseCase
) = produceState(
    initialValue = Profit(0f, 0f, 0f),
) {
    useCase.getProfit().collect {
        value = it
    }
}

@Composable
private fun lastReceiptsAsState(
    useCase: HomeUseCase
) = produceState(
    initialValue = emptyList<ReceiptItem>()
) {
    useCase.getLastReceipts(limit = 5).collect {
        value = it
    }
}

@Composable
private fun mostSaleAsState(
    useCase: HomeUseCase
) = produceState(
    initialValue = emptyList<MostSaleItem>()
) {
    useCase.getMostSale(limit = 5).collect {
        value = it
    }
}