package com.debk007.template.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.debk007.template.presentation.screen.DetailScreen
import com.debk007.template.presentation.screen.HomeScreen

@Serializable
object Home

@Serializable
data class Detail(val id: Int)

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Home) {

        composable<Home> {
            HomeScreen(navController = navController)
        }
        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()

            DetailScreen(navController = navController, id = detail.id)
        }
    }
}