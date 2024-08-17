package com.bruno13palhano.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno13palhano.home.ui.HomeRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToHome() = navigate(HomeRoutes.Home)

fun NavGraphBuilder.homeScreen() {
    composable<HomeRoutes.Home> {
        HomeRoute()
    }
}

sealed interface HomeRoutes {
    @Serializable
    data object Home : HomeRoutes
}