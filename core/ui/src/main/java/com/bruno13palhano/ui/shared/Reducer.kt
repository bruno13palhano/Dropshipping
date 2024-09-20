package com.bruno13palhano.ui.shared

interface Reducer<State: ViewState, Event: ViewEvent, Effect: ViewEffect> {
    fun reduce(previousState: State, event: Event): Pair<State, Effect?>
}