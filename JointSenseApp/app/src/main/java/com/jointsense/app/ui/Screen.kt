package com.jointsense.app.ui

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Profile : Screen("profile")
    object Questionnaire : Screen("questionnaire")
    object DataCollection : Screen("data_collection")
    object Results : Screen("results/{riskScore}") {
        fun createRoute(score: Float) = "results/$score"
    }
    object History : Screen("history")
}
