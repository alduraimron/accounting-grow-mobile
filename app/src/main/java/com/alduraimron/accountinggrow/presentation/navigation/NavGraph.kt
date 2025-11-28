package com.alduraimron.accountinggrow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alduraimron.accountinggrow.presentation.auth.LoginScreen
import com.alduraimron.accountinggrow.presentation.auth.RegisterScreen
import com.alduraimron.accountinggrow.presentation.budget.AddBudgetScreen
import com.alduraimron.accountinggrow.presentation.budget.BudgetScreen
import com.alduraimron.accountinggrow.presentation.home.HomeScreen
import com.alduraimron.accountinggrow.presentation.onboarding.OnboardingScreen
import com.alduraimron.accountinggrow.presentation.profile.ProfileScreen
import com.alduraimron.accountinggrow.presentation.reminder.AddReminderScreen
import com.alduraimron.accountinggrow.presentation.reminder.ReminderScreen
import com.alduraimron.accountinggrow.presentation.saving.AddSavingScreen
import com.alduraimron.accountinggrow.presentation.saving.SavingScreen
import com.alduraimron.accountinggrow.presentation.splash.SplashScreen
import com.alduraimron.accountinggrow.presentation.transaction.AddTransactionScreen
import com.alduraimron.accountinggrow.presentation.transaction.TransactionListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding Screen
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Register Screen
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToTransactions = {
                    navController.navigate(Screen.TransactionList.route)
                },
                onNavigateToSavings = {
                    navController.navigate(Screen.Saving.route)
                },
                onNavigateToBudgets = {
                    navController.navigate(Screen.Budget.route)
                },
                onNavigateToReminders = {
                    navController.navigate(Screen.Reminder.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // Transaction List Screen
        composable(route = Screen.TransactionList.route) {
            TransactionListScreen(
                onBack = { navController.navigateUp() },
                onAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                }
            )
        }

        // Add Transaction Screen
        composable(route = Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBack = { navController.navigateUp() },
                onSuccess = { navController.navigateUp() }
            )
        }

        // Saving Screen
        composable(route = Screen.Saving.route) {
            SavingScreen(
                onBack = { navController.navigateUp() },
                onAddSaving = {
                    navController.navigate(Screen.AddSaving.route)
                }
            )
        }

        // Add Saving Screen
        composable(route = Screen.AddSaving.route) {
            AddSavingScreen(
                onBack = { navController.navigateUp() },
                onSuccess = { navController.navigateUp() }
            )
        }

        // Budget Screen
        composable(route = Screen.Budget.route) {
            BudgetScreen(
                onBack = { navController.navigateUp() },
                onAddBudget = {
                    navController.navigate(Screen.AddBudget.route)
                }
            )
        }

        // Add Budget Screen
        composable(route = Screen.AddBudget.route) {
            AddBudgetScreen(
                onBack = { navController.navigateUp() },
                onSuccess = { navController.navigateUp() }
            )
        }

        // Reminder Screen
        composable(route = Screen.Reminder.route) {
            ReminderScreen(
                onBack = { navController.navigateUp() },
                onAddReminder = {
                    navController.navigate(Screen.AddReminder.route)
                }
            )
        }

        // Add Reminder Screen
        composable(route = Screen.AddReminder.route) {
            AddReminderScreen(
                onBack = { navController.navigateUp() },
                onSuccess = { navController.navigateUp() }
            )
        }

        // Profile Screen
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.navigateUp() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}