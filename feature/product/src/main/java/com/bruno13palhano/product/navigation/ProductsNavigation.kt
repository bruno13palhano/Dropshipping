package com.bruno13palhano.product.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno13palhano.product.ui.screen.ProductsRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.productsScreen(modifier: Modifier = Modifier) {
    composable<ProductsRoutes.Products> {
        ProductsRoute(modifier = modifier)
    }
}

sealed interface ProductsRoutes {
    @Serializable
    data object Products : ProductsRoutes
}