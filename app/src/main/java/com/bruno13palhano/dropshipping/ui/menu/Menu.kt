package com.bruno13palhano.dropshipping.ui.menu

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.dropshipping.R
import com.bruno13palhano.home.navigation.HomeRoutes
import com.bruno13palhano.product.navigation.ProductsRoutes
import com.bruno13palhano.receipt.navigation.ReceiptsRoutes

@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val items = listOf(
        Screen.Home,
        Screen.Receipts,
        Screen.Products
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar(modifier = modifier) {
        items.forEach { screen ->
            val selected = currentDestination?.isRouteSelected(screen)
            
            NavigationBarItem(
                selected = selected == true,
                onClick = { 
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.resourceId)
                    )
                },
                label = { Text(text = stringResource(id = screen.resourceId)) }
            )
        }
    }
}

fun <T>NavDestination.isRouteSelected(screen: Screen<T>): Boolean {
    return hierarchy.any { 
        it.route?.split(".")?.lastOrNull() == screen.route.toString()
    }
}

sealed class Screen<T>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Home: Screen<HomeRoutes>(
        route = HomeRoutes.Home,
        icon = Icons.Filled.Home,
        resourceId = R.string.home
    )
    data object Receipts: Screen<ReceiptsRoutes>(
        route = ReceiptsRoutes.Main,
        icon = Icons.AutoMirrored.Filled.ListAlt,
        resourceId = R.string.receipts
    )
    data object Products: Screen<ProductsRoutes>(
        route = ProductsRoutes.Products,
        icon = Icons.AutoMirrored.Filled.List,
        resourceId = R.string.products
    )
}