package com.bruno13palhano.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno13palhano.home.ui.HomeRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeScreen(modifier: Modifier = Modifier) {
    composable<HomeRoutes.Home> {
        HomeRoute(modifier = modifier)
    }
}

sealed interface HomeRoutes {
    @Serializable
    data object Home : HomeRoutes
}