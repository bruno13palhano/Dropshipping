package com.bruno13palhano.home.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.domain.HomeUseCase
import com.bruno13palhano.home.ui.shared.HomeEffect
import com.bruno13palhano.home.ui.shared.HomeEvent
import com.bruno13palhano.ui.shared.BaseViewModel
import com.bruno13palhano.home.ui.shared.HomeReducer
import com.bruno13palhano.home.ui.shared.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState.INITIAL_STATE,
    reducer = HomeReducer()
) {
    fun getProfit() {
        viewModelScope.launch {
            homeUseCase.getProfit()
                .collect {
                    sendEvent(
                        HomeEvent.UpdateProfit(
                            profit = it
                        )
                    )

                    sendEvent(HomeEvent.UpdateReceiptsVisibility(visible = true))
                }
        }
    }

    fun getLastReceipts() {
        viewModelScope.launch {
            homeUseCase.getLastReceipts(limit = 5).collect {
                sendEvent(HomeEvent.UpdateLastReceipts(lastReceipts = it))
            }
        }
    }

    fun getMostSale() {
        viewModelScope.launch {
            homeUseCase.getMostSale(limit = 5).collect {
                sendEvent(HomeEvent.UpdateMostSale(mostSale = it))
            }
        }
    }
}