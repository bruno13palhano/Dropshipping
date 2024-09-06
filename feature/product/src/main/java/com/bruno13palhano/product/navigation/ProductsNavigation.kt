package com.bruno13palhano.product.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.product.ui.screen.ProductRoute
import com.bruno13palhano.product.ui.screen.ProductsRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.productsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    navigation<ProductsRoutes.MainProduct>(startDestination = ProductsRoutes.Products) {
        composable<ProductsRoutes.Products> {
            ProductsRoute(
                modifier = modifier,
                navigateToEditProduct = { id ->
                    navController.navigate(ProductsRoutes.UpdateProduct(id))
                },
                navigateToAddProduct = {
                    navController.navigate(ProductsRoutes.AddProduct)
                }
            )
        }
        composable<ProductsRoutes.UpdateProduct> {
            val id = it.toRoute<ProductsRoutes.UpdateProduct>().id

            ProductRoute(
                modifier = modifier,
                id = id,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ProductsRoutes.AddProduct> {
            ProductRoute(
                modifier = modifier,
                id = 0L,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}

sealed interface ProductsRoutes {
    @Serializable
    data object MainProduct: ProductsRoutes

    @Serializable
    data object Products : ProductsRoutes

    @Serializable
    data class UpdateProduct(val id: Long) : ProductsRoutes

    @Serializable
    data object AddProduct : ProductsRoutes
}