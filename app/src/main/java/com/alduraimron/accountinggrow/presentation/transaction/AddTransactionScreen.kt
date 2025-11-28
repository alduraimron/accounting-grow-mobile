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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.TransactionType
import com.alduraimron.accountinggrow.presentation.category.AddCategoryDialog
import com.alduraimron.accountinggrow.ui.theme.AccountingGrowTheme
import com.alduraimron.accountinggrow.ui.theme.ExpenseRed
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.IncomeGreen
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    var nominal by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val transactionState by viewModel.transactionState.collectAsState()

    // Load categories on init
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    // Navigate back on success
    LaunchedEffect(transactionState.isTransactionCreated) {
        if (transactionState.isTransactionCreated) {
            viewModel.resetTransactionCreated()
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tambah Transaksi") },
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
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Type Selection
                Text(
                    text = "Tipe Transaksi",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Income Button
                    TypeButton(
                        text = "Pemasukan",
                        isSelected = selectedType == TransactionType.INCOME,
                        color = IncomeGreen,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedType = TransactionType.INCOME
                            selectedCategory = null // Reset category on type change
                        }
                    )

                    // Expense Button
                    TypeButton(
                        text = "Pengeluaran",
                        isSelected = selectedType == TransactionType.EXPENSE,
                        color = ExpenseRed,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedType = TransactionType.EXPENSE
                            selectedCategory = null // Reset category on type change
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nominal
                OutlinedTextField(
                    value = nominal,
                    onValueChange = { nominal = it.filter { char -> char.isDigit() } },
                    label = { Text("Nominal") },
                    placeholder = { Text("Masukkan nominal") },
                    prefix = { Text("Rp ") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Gray500.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryBlue,
                        cursorColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !transactionState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Dropdown
                Column {
                    Box {
                        OutlinedTextField(
                            value = selectedCategory?.name ?: "",
                            onValueChange = {},
                            label = { Text("Kategori") },
                            placeholder = { Text("Pilih kategori") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCategoryDropdown = true },
                            readOnly = true,
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown"
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Gray500.copy(alpha = 0.5f),
                                disabledLabelColor = Gray500,
                                disabledTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            val filteredCategories = transactionState.categories.filter {
                                it.type == selectedType
                            }

                            if (filteredCategories.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Belum ada kategori") },
                                    onClick = { }
                                )
                            } else {
                                filteredCategories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            selectedCategory = category
                                            showCategoryDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Add Category Button
                    TextButton(
                        onClick = { showAddCategoryDialog = true },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "+ Tambah Kategori Baru",
                            color = PrimaryBlue,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date Picker
                OutlinedTextField(
                    value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    onValueChange = {},
                    label = { Text("Tanggal") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Calendar")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Gray500.copy(alpha = 0.5f),
                        disabledLabelColor = Gray500,
                        disabledTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi (Opsional)") },
                    placeholder = { Text("Masukkan deskripsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Gray500.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryBlue,
                        cursorColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !transactionState.isLoading
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (selectedCategory != null && nominal.isNotBlank()) {
                            viewModel.createTransaction(
                                categoryId = selectedCategory!!.id,
                                type = if (selectedType == TransactionType.INCOME) "INCOME" else "EXPENSE",
                                nominal = nominal.toDoubleOrNull() ?: 0.0,
                                description = description.ifBlank { null },
                                date = selectedDate.toString() // Format: YYYY-MM-DD
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(25.dp),
                    enabled = !transactionState.isLoading &&
                            selectedCategory != null &&
                            nominal.isNotBlank()
                ) {
                    if (transactionState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Text(
                            text = "Simpan Transaksi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Add Category Dialog
        if (showAddCategoryDialog) {
            AddCategoryDialog(
                onDismiss = { showAddCategoryDialog = false },
                onCreate = { name, type ->
                    viewModel.createCategory(name, type)
                    showAddCategoryDialog = false
                },
                isLoading = transactionState.isLoading
            )
        }

        // Simple Date Picker Dialog
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                title = { Text("Pilih Tanggal") },
                text = {
                    Column {
                        TextButton(
                            onClick = {
                                selectedDate = LocalDate.now()
                                showDatePicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Hari Ini")
                        }
                        TextButton(
                            onClick = {
                                selectedDate = LocalDate.now().minusDays(1)
                                showDatePicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Kemarin")
                        }
                        TextButton(
                            onClick = {
                                selectedDate = LocalDate.now().minusDays(2)
                                showDatePicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("2 Hari yang Lalu")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Tutup")
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
fun TypeButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionScreenPreview() {
    AccountingGrowTheme {
        AddTransactionScreen(
            onBack = {},
            onSuccess = {}
        )
    }
}