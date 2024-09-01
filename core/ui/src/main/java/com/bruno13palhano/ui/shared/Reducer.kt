package com.bruno13palhano.ui.shared

interface Reducer<State: Reducer.ViewState, Event: Reducer.ViewEvent, Effect: Reducer.ViewEffect> {
    interface ViewState

    interface ViewEvent

    interface ViewEffect

    fun reduce(state: State, event: Event): Pair<State, Effect?>
}