package com.bruno13palhano.product.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.product.ui.shared.ProductsEffect
import com.bruno13palhano.product.ui.shared.ProductsEvent
import com.bruno13palhano.product.ui.shared.ProductsReducer
import com.bruno13palhano.product.ui.shared.ProductsState
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): BaseViewModel<ProductsState, ProductsEvent, ProductsEffect>(
    initialState = ProductsState.INITIAL_STATE,
    reducer = ProductsReducer()
) {
    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect { productList: List<Product> ->
                sendEvent(
                    event = ProductsEvent.UpdateProducts(
                        isLoading = true,
                        products = productList.map { product ->
                            CommonItem(
                                id = product.id,
                                title = product.name
                            )
                        }
                    )
                )
            }
        }
    }

    fun updatingProductState(id: Long) {
        sendEvent(
            event = ProductsEvent.EditProduct(
                editProduct = true,
                id = id
            )
        )
    }

    fun addButtonClick() {
        sendEvent(event = ProductsEvent.AddProduct(true))
    }

    fun onDeletingProductClick(id: Long) {
        sendEvent(event = ProductsEvent.UpdateDeletingProduct(isDeleting = true, id = id))
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            productRepository.delete(id = id)
            sendEvent(event = ProductsEvent.OnDeleteProductSuccessfully)
        }
    }
}