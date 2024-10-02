package com.bruno13palhano.receipt.ui.search

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class SearchState(
    val query: String,
    val active: Boolean,
    val insert: Boolean,
    val delete: Boolean,
    val products: List<CommonItem>,
    val cache: List<String>
) : ViewState {
    companion object {
        val INITIAL_STATE = SearchState(
            query = "",
            active = false,
            insert = false,
            delete = false,
            products = emptyList(),
            cache = emptyList()
        )
    }
}

@Immutable
internal sealed interface SearchEvent : ViewEvent {
    data class Delete(val query: String) : SearchEvent
    data class Active(val active: Boolean) : SearchEvent
    data class Done(val query: String) : SearchEvent
    data object Close : SearchEvent
    data class ProductItem(val id: Long) : SearchEvent
    data class UpdateCache(val cache: List<String>) : SearchEvent
    data class UpdateProducts(val products: List<CommonItem>) : SearchEvent
}

@Immutable
internal sealed interface SearchEffect : ViewEffect {
    data class NavigateToAddReceipt(val productId: Long) : SearchEffect
    data object NavigateBack : SearchEffect
}

@Immutable
internal sealed interface SearchAction : ViewAction {
    data class OnDeleteClick(val query: String) : SearchAction
    data class OnActiveChange(val active: Boolean) : SearchAction
    data class OnDoneClick(val query: String) : SearchAction
    data object OnCloseClick : SearchAction
    data class OnProductItemClick(val id: Long) : SearchAction
}