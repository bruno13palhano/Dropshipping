package com.bruno13palhano.receipt.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.receipt.ui.screen.AddReceiptRoute
import com.bruno13palhano.receipt.ui.screen.ReceiptsRoute
import com.bruno13palhano.receipt.ui.screen.SearchRoute
import com.bruno13palhano.receipt.ui.screen.UpdateReceiptRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.receiptsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    navigation<ReceiptsRoutes.MainReceipt>(startDestination = ReceiptsRoutes.Receipts) {
        composable<ReceiptsRoutes.Receipts> {
            ReceiptsRoute(
                modifier = modifier,
                onItemClick = {
                    navController.navigate(ReceiptsRoutes.UpdateReceipt(id = it))
                },
                onAddNewReceiptClick = {
                    navController.navigate(ReceiptsRoutes.SearchProduct)
                }
            )
        }
        composable<ReceiptsRoutes.SearchProduct> {
            SearchRoute(
                modifier = modifier,
                navigateToAddReceipt = {
                    navController.navigate(ReceiptsRoutes.AddReceipt(productId = it))
                },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable<ReceiptsRoutes.AddReceipt> {
            val productId = it.toRoute<ReceiptsRoutes.AddReceipt>().productId

            AddReceiptRoute(
                modifier = modifier,
                productId = productId,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<ReceiptsRoutes.UpdateReceipt> {
            val id = it.toRoute<ReceiptsRoutes.UpdateReceipt>().id

            UpdateReceiptRoute(
                modifier = modifier,
                id = id,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}

sealed interface ReceiptsRoutes {
    @Serializable
    data object MainReceipt: ReceiptsRoutes

    @Serializable
    data object Receipts : ReceiptsRoutes

    @Serializable
    data object SearchProduct : ReceiptsRoutes

    @Serializable
    data class AddReceipt(val productId: Long) : ReceiptsRoutes

    @Serializable
    data class UpdateReceipt(val id: Long) : ReceiptsRoutes
}