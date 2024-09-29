package com.bruno13palhano.home.ui

import androidx.compose.runtime.Composable
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel<HomeState, HomeAction, HomeEvent, HomeEffect>(
    actionProcessor = HomeActionProcessor(),
    reducer = HomeReducer()
) {
    @Composable
    override fun states(events: Flow<HomeEvent>): HomeState {
        return homePresenter(
            reducer = reducer,
            profit = homeUseCase.getProfit(),
            lastReceipts = homeUseCase.getLastReceipts(limit = 5),
            mostSale = homeUseCase.getMostSale(limit = 5),
            sendEvent = ::sendEvent,
            events = events
        )
    }
}