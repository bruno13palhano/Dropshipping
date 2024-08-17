package com.bruno13palhano.product.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno13palhano.product.ui.ProductsRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToProducts() = navigate(ProductsRoutes.Products)

fun NavGraphBuilder.productsScreen(modifier: Modifier = Modifier) {
    composable<ProductsRoutes.Products> {
        ProductsRoute(modifier = modifier)
    }
}

sealed interface ProductsRoutes {
    @Serializable
    data object Products : ProductsRoutes
}