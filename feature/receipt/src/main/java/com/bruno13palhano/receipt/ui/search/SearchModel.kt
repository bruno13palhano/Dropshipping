package com.bruno13palhano.receipt.ui.search

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class SearchState(
    val deleting: Boolean,
    val updatingCache: Boolean,
    val updatingProducts: Boolean,
    val query: String,
    val active: Boolean,
    val products: List<CommonItem>,
    val cache: List<String>
) : ViewState {
    companion object {
        val INITIAL_STATE = SearchState(
            deleting = false,
            updatingCache = false,
            updatingProducts = false,
            query = "",
            active = false,
            products = emptyList(),
            cache = emptyList()
        )
    }
}

@Immutable
internal sealed interface SearchEvent : ViewEvent {
    data class UpdateDeleting(val deleting: Boolean, val query: String) : SearchEvent
    data class UpdateActive(val active: Boolean) : SearchEvent
    data class OnSearchDoneClick(val query: String) : SearchEvent
    data object OnCloseSearchClick : SearchEvent
    data class OnProductItemClick(val id: Long) : SearchEvent
}

@Immutable
internal sealed interface SearchEffect : ViewEffect {
    data class NavigateToAddReceipt(val productId: Long) : SearchEffect
    data object NavigateBack : SearchEffect
}

internal sealed interface SearchAction : ViewAction {
    data class DeleteSearchClick(val deleting: Boolean, val query: String) : SearchAction
    data class OnActiveChange(val active: Boolean) : SearchAction
    data class OnSearchDoneClick(val query: String) : SearchAction
    data object OnCloseSearchClick : SearchAction
    data class OnProductItemClick(val id: Long) : SearchAction
}