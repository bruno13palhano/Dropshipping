package com.bruno13palhano.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode.ContextClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State: Reducer.ViewState, Event: Reducer.ViewEvent, Effect: Reducer.ViewEffect> : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val events: MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = 20)

    private val _effect = Channel<Effect>(capacity = Channel.CONFLATED)
    val effects = _effect.receiveAsFlow()

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(mode = ContextClock) {
            states(events, effects)
        }
    }

    fun sendEffect(effect: Effect) {
        _effect.trySend(effect)
    }

    fun sendEvent(event: Event) {
        events.tryEmit(event)
    }

    @Composable
    protected abstract fun states(events: Flow<Event>, effects: Flow<Effect>?): State
}