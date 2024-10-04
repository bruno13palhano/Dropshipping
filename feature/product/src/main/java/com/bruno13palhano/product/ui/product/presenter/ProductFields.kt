package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bruno13palhano.model.Product
import javax.inject.Inject

/**
 * An wrapper for Product input fields.
 */
internal class ProductFields @Inject constructor() {
    var naturaCode by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set

    fun updateNaturaCode(naturaCode: String) { this.naturaCode = naturaCode }

    fun updateProductName(productName: String) { this.productName = productName }

    fun isValid(): Boolean {
        return naturaCode.isNotBlank() && productName.isNotBlank()
    }

    fun toProduct(id: Long = 0L): Product {
        return Product(
            id = id,
            naturaCode = naturaCode,
            name = productName
        )
    }

    fun setFields(product: Product) {
        updateNaturaCode(naturaCode = product.naturaCode)
        updateProductName(productName = product.name)
    }
}