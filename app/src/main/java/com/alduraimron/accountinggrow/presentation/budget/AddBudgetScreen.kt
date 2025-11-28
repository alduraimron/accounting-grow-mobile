package com.alduraimron.accountinggrow.presentation.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.TransactionType
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedBudgetType by remember { mutableStateOf("MONTHLY") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var showBudgetTypeDropdown by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    val budgetState by viewModel.budgetState.collectAsState()
    val now = LocalDate.now()

    LaunchedEffect(budgetState.isBudgetCreated) {
        if (budgetState.isBudgetCreated) {
            viewModel.resetBudgetCreated()
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Anggaran") },
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
            // Category Dropdown
            Text(
                text = "Kategori",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "",
                    onValueChange = {},
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
                        disabledTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                DropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    val expenseCategories = budgetState.categories.filter {
                        it.type == TransactionType.EXPENSE
                    }

                    if (expenseCategories.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Belum ada kategori pengeluaran") },
                            onClick = { }
                        )
                    } else {
                        expenseCategories.forEach { category ->
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

            Spacer(modifier = Modifier.height(16.dp))

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() } },
                label = { Text("Jumlah Anggaran") },
                placeholder = { Text("Masukkan jumlah") },
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
                enabled = !budgetState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Budget Type Dropdown
            Text(
                text = "Tipe Anggaran",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = when (selectedBudgetType) {
                        "DAILY" -> "Harian"
                        "WEEKLY" -> "Mingguan"
                        "MONTHLY" -> "Bulanan"
                        else -> "Bulanan"
                    },
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBudgetTypeDropdown = true },
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
                        disabledTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                DropdownMenu(
                    expanded = showBudgetTypeDropdown,
                    onDismissRequest = { showBudgetTypeDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text = { Text("Harian") },
                        onClick = {
                            selectedBudgetType = "DAILY"
                            showBudgetTypeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Mingguan") },
                        onClick = {
                            selectedBudgetType = "WEEKLY"
                            showBudgetTypeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Bulanan") },
                        onClick = {
                            selectedBudgetType = "MONTHLY"
                            showBudgetTypeDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan (Opsional)") },
                placeholder = { Text("Tambahkan catatan") },
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
                enabled = !budgetState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (selectedCategory != null && amount.isNotBlank()) {
                        viewModel.createBudget(
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            budgetType = selectedBudgetType,
                            month = now.monthValue,
                            year = now.year,
                            notes = notes.ifBlank { null },
                            categoryId = selectedCategory!!.id
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
                enabled = !budgetState.isLoading && selectedCategory != null && amount.isNotBlank()
            ) {
                if (budgetState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Text(
                        text = "Simpan Anggaran",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}