package com.bruno13palhano.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.model.Profit
import kotlinx.coroutines.flow.Flow

@Composable
internal fun homePresenter(
    useCase: HomeUseCase,
    events: Flow<HomeEvent>
): HomeState {
    val profit by useCase.getProfit().collectAsState(initial = Profit(0f, 0f, 0f))
    val lastReceipts by useCase.getLastReceipts(limit = 5).collectAsState(initial = emptyList())
    val mostSale by useCase.getMostSale(limit = 5).collectAsState(initial = emptyList())
    var profitVisible by remember { mutableStateOf(false) }
    var receiptsVisible by remember { mutableStateOf(false) }
    var expandedItems by remember { mutableStateOf(emptyList<Pair<Long, Boolean>>()) }

    LaunchedEffect(lastReceipts.isNotEmpty()) {
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
        profit = profit,
        lastReceipts = lastReceipts,
        mostSale = mostSale,
        expandedItems = expandedItems
    )
}