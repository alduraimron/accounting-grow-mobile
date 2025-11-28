package com.alduraimron.accountinggrow.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alduraimron.accountinggrow.domain.model.Transaction
import com.alduraimron.accountinggrow.domain.model.TransactionType
import com.alduraimron.accountinggrow.ui.theme.AccountingGrowTheme
import com.alduraimron.accountinggrow.ui.theme.ExpenseRed
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.IncomeGreen
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlueLight
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToTransactions: () -> Unit = {},
    onNavigateToSavings: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    username = homeState.username,
                    onRefresh = { viewModel.loadHomeData() }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { index ->
                        selectedTab = index
                        when (index) {
                            0 -> { /* Already on Home */ }
                            1 -> onNavigateToTransactions()
                            2 -> onNavigateToBudgets()
                            3 -> onNavigateToSavings()
                            4 -> onNavigateToProfile()
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (homeState.isLoading && homeState.summary == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF8F9FA))
                ) {
                    item {
                        // Balance Card
                        BalanceCard(
                            balance = homeState.summary?.balance ?: 0.0,
                            totalIncome = homeState.summary?.totalIncome ?: 0.0,
                            totalExpense = homeState.summary?.totalExpense ?: 0.0
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Quick Access Menu
                        QuickAccessMenu(
                            onTransactionsClick = onNavigateToTransactions,
                            onSavingsClick = onNavigateToSavings,
                            onBudgetsClick = onNavigateToBudgets,
                            onRemindersClick = onNavigateToReminders
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        // Recent Transactions Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Transaksi Terakhir",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Lihat Semua",
                                fontSize = 14.sp,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { onNavigateToTransactions() }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (homeState.recentTransactions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada transaksi",
                                    color = Gray500,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(homeState.recentTransactions) { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Error Message
        homeState.errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Text(text = error)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                unselectedIconColor = Gray500,
                unselectedTextColor = Gray500,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Transaksi"
                )
            },
            label = { Text("Transaksi") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                unselectedIconColor = Gray500,
                unselectedTextColor = Gray500,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Anggaran"
                )
            },
            label = { Text("Anggaran") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                unselectedIconColor = Gray500,
                unselectedTextColor = Gray500,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = "Tabungan"
                )
            },
            label = { Text("Tabungan") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                unselectedIconColor = Gray500,
                unselectedTextColor = Gray500,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profil"
                )
            },
            label = { Text("Profil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                unselectedIconColor = Gray500,
                unselectedTextColor = Gray500,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun HomeTopBar(
    username: String,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(PrimaryBlue, PrimaryBlueLight)
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Halo, ",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = username.ifEmpty { "User" },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun QuickAccessMenu(
    onTransactionsClick: () -> Unit,
    onSavingsClick: () -> Unit,
    onBudgetsClick: () -> Unit,
    onRemindersClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Menu Cepat",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessCard(
                title = "Transaksi",
                icon = Icons.Default.Receipt,
                color = PrimaryBlue,
                modifier = Modifier.weight(1f),
                onClick = onTransactionsClick
            )
            QuickAccessCard(
                title = "Tabungan",
                icon = Icons.Default.Savings,
                color = IncomeGreen,
                modifier = Modifier.weight(1f),
                onClick = onSavingsClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessCard(
                title = "Anggaran",
                icon = Icons.Default.AccountBalanceWallet,
                color = Color(0xFFFFA500),
                modifier = Modifier.weight(1f),
                onClick = onBudgetsClick
            )
            QuickAccessCard(
                title = "Pengingat",
                icon = Icons.Default.Notifications,
                color = ExpenseRed,
                modifier = Modifier.weight(1f),
                onClick = onRemindersClick
            )
        }
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
fun BalanceCard(
    balance: Double,
    totalIncome: Double,
    totalExpense: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Balance
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Balance",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Total Saldo",
                    fontSize = 14.sp,
                    color = Gray500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatCurrency(balance),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Income & Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Income
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(IncomeGreen.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Income",
                                tint = IncomeGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pemasukan",
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCurrency(totalIncome),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IncomeGreen
                    )
                }

                // Expense
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ExpenseRed.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Expense",
                                tint = ExpenseRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pengeluaran",
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCurrency(totalExpense),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ExpenseRed
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (transaction.type == TransactionType.INCOME)
                            IncomeGreen.copy(alpha = 0.1f)
                        else
                            ExpenseRed.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type == TransactionType.INCOME)
                        Icons.Default.ArrowDownward
                    else
                        Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (transaction.type == TransactionType.INCOME)
                        IncomeGreen
                    else
                        ExpenseRed,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.category?.name ?: "Tanpa Kategori",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                if (!transaction.description.isNullOrEmpty()) {
                    Text(
                        text = transaction.description,
                        fontSize = 12.sp,
                        color = Gray500
                    )
                }
                Text(
                    text = formatDate(transaction.date),
                    fontSize = 11.sp,
                    color = Gray500
                )
            }

            // Amount
            Text(
                text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"} ${formatCurrency(transaction.nominal)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.INCOME)
                    IncomeGreen
                else
                    ExpenseRed
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("Rp", "Rp ")
}

fun formatDate(dateString: String): String {
    return try {
        dateString.substring(0, 10)
    } catch (e: Exception) {
        dateString
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AccountingGrowTheme {
        HomeScreen()
    }
}