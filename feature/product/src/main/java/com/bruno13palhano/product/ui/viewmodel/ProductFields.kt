package com.bruno13palhano.product.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * An wrapper for Product input fields.
 */
internal class ProductFields {
    var naturaCode by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set

    fun updateNaturaCode(naturaCode: String) { this.naturaCode = naturaCode }

    fun updateProductName(productName: String) { this.productName = productName }
}