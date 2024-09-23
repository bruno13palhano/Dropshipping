package com.bruno13palhano.home.ui

import com.bruno13palhano.ui.shared.ActionProcessor

internal class HomeActionProcessor : ActionProcessor<HomeAction, HomeEvent> {
    override fun processAction(viewAction: HomeAction): HomeEvent {
        return when (viewAction) {
            is HomeAction.OnProfitVisibilityChanged -> {
                HomeEvent.ProfitVisibility(visible = viewAction.visible)
            }

            is HomeAction.OnReceiptsVisibilityChanged -> {
                HomeEvent.ReceiptsVisibility(visible = viewAction.visible)
            }

            is HomeAction.OnExpandedItemChanged -> {
                HomeEvent.ExpandedItem(id = viewAction.id, expanded = viewAction.expanded)
            }
        }
    }
}