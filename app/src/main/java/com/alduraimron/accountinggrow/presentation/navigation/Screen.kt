package com.alduraimron.accountinggrow.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object TransactionList : Screen("transaction_list")
    object AddTransaction : Screen("add_transaction")
    object Saving : Screen("saving")
    object AddSaving : Screen("add_saving")
    object Budget : Screen("budget")
    object AddBudget : Screen("add_budget")
    object Reminder : Screen("reminder")
    object AddReminder : Screen("add_reminder")
    object Profile : Screen("profile")
}