package com.bruno13palhano.dropshipping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bruno13palhano.home.navigation.HomeRoutes
import com.bruno13palhano.home.navigation.homeScreen
import com.bruno13palhano.product.navigation.productsScreen
import com.bruno13palhano.receipt.navigation.receiptsScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutes.Home
    ) {
        homeScreen(modifier = modifier)
        receiptsScreen(modifier = modifier, navController = navController)
        productsScreen(modifier = modifier)
    }
}