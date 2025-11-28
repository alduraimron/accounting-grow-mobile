package com.alduraimron.accountinggrow.presentation.saving

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: SavingViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var currentAmount by remember { mutableStateOf("") }
    var selectedFillingPlan by remember { mutableStateOf("MONTHLY") }
    var showFillingPlanDropdown by remember { mutableStateOf(false) }

    val savingState by viewModel.savingState.collectAsState()

    LaunchedEffect(savingState.isSavingCreated) {
        if (savingState.isSavingCreated) {
            viewModel.resetSavingCreated()
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Tabungan") },
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
            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Tabungan") },
                placeholder = { Text("Contoh: Dana Darurat, Liburan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Gray500.copy(alpha = 0.5f),
                    focusedLabelColor = PrimaryBlue,
                    cursorColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !savingState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Target Amount
            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it.filter { char -> char.isDigit() } },
                label = { Text("Target Tabungan") },
                placeholder = { Text("Masukkan target") },
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
                enabled = !savingState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Current Amount
            OutlinedTextField(
                value = currentAmount,
                onValueChange = { currentAmount = it.filter { char -> char.isDigit() } },
                label = { Text("Saldo Awal (Opsional)") },
                placeholder = { Text("Masukkan saldo awal") },
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
                enabled = !savingState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filling Plan Dropdown
            Text(
                text = "Rencana Pengisian",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = when (selectedFillingPlan) {
                        "DAILY" -> "Harian"
                        "WEEKLY" -> "Mingguan"
                        "MONTHLY" -> "Bulanan"
                        else -> "Bulanan"
                    },
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFillingPlanDropdown = true },
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
                    expanded = showFillingPlanDropdown,
                    onDismissRequest = { showFillingPlanDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text = { Text("Harian") },
                        onClick = {
                            selectedFillingPlan = "DAILY"
                            showFillingPlanDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Mingguan") },
                        onClick = {
                            selectedFillingPlan = "WEEKLY"
                            showFillingPlanDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Bulanan") },
                        onClick = {
                            selectedFillingPlan = "MONTHLY"
                            showFillingPlanDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (name.isNotBlank() && targetAmount.isNotBlank()) {
                        viewModel.createSaving(
                            name = name,
                            targetAmount = targetAmount.toDoubleOrNull() ?: 0.0,
                            currentAmount = currentAmount.toDoubleOrNull() ?: 0.0,
                            fillingPlan = selectedFillingPlan
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
                enabled = !savingState.isLoading && name.isNotBlank() && targetAmount.isNotBlank()
            ) {
                if (savingState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Text(
                        text = "Simpan Tabungan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}