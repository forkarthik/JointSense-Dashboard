package com.jointsense.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jointsense.app.ui.screens.*

@Composable
fun JointSenseNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Questionnaire.route) {
            QuestionnaireScreen(navController)
        }
        composable(Screen.DataCollection.route) {
            DataCollectionScreen(navController)
        }
        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("riskScore") { type = NavType.FloatType })
        ) { backStackEntry ->
            val riskScore = backStackEntry.arguments?.getFloat("riskScore") ?: 0f
            ResultsScreen(navController, riskScore)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController)
        }
    }
}
