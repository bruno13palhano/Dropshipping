package com.bruno13palhano.home.ui.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.home.ui.presenter.HomeAction
import com.bruno13palhano.home.ui.presenter.HomeActionProcessor
import com.bruno13palhano.home.ui.presenter.HomeEffect
import com.bruno13palhano.home.ui.presenter.HomeEvent
import com.bruno13palhano.home.ui.presenter.HomeReducer
import com.bruno13palhano.home.ui.presenter.HomeState
import com.bruno13palhano.home.ui.presenter.homePresenter
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