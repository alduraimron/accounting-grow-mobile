package com.alduraimron.accountinggrow.presentation.transaction

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onBack: () -> Unit,
    onAddTransaction: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionState by viewModel.transactionState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Transaksi") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Tambah Transaksi")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F9FA))
            ) {
                // Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = transactionState.filterType == null,
                        onClick = { viewModel.loadTransactions(null) },
                        label = { Text("Semua") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = transactionState.filterType == "INCOME",
                        onClick = { viewModel.loadTransactions("INCOME") },
                        label = { Text("Pemasukan") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IncomeGreen,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = transactionState.filterType == "EXPENSE",
                        onClick = { viewModel.loadTransactions("EXPENSE") },
                        label = { Text("Pengeluaran") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ExpenseRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                // Transaction List
                if (transactionState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                } else if (transactionState.transactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Belum ada transaksi",
                                fontSize = 16.sp,
                                color = Gray500
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap tombol + untuk menambah",
                                fontSize = 14.sp,
                                color = Gray500
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(transactionState.transactions) { transaction ->
                            TransactionItemCard(
                                transaction = transaction,
                                onDelete = {
                                    transactionToDelete = transaction
                                    showDeleteDialog = true
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && transactionToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Hapus Transaksi") },
                text = { Text("Apakah Anda yakin ingin menghapus transaksi ini?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTransaction(transactionToDelete!!.id)
                            showDeleteDialog = false
                            transactionToDelete = null
                        }
                    ) {
                        Text("Hapus", color = ExpenseRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }

        // Error Message
        transactionState.errorMessage?.let { error ->
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
fun TransactionItemCard(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"} ${formatCurrency(transaction.nominal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME)
                        IncomeGreen
                    else
                        ExpenseRed
                )

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ExpenseRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
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
fun TransactionListScreenPreview() {
    AccountingGrowTheme {
        TransactionListScreen(
            onBack = {},
            onAddTransaction = {}
        )
    }
}