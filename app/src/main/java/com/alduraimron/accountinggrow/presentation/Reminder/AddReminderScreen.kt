package com.alduraimron.accountinggrow.presentation.reminder

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedRecurrence by remember { mutableStateOf("NONE") }
    var selectedDate by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var showRecurrenceDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val reminderState by viewModel.reminderState.collectAsState()

    LaunchedEffect(reminderState.isReminderCreated) {
        if (reminderState.isReminderCreated) {
            viewModel.resetReminderCreated()
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Pengingat") },
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
                label = { Text("Nama Pengingat") },
                placeholder = { Text("Contoh: Tagihan Listrik") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Gray500.copy(alpha = 0.5f),
                    focusedLabelColor = PrimaryBlue,
                    cursorColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !reminderState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() } },
                label = { Text("Jumlah") },
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
                enabled = !reminderState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Due Date with Material3 DatePicker
            OutlinedTextField(
                value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = {},
                label = { Text("Tanggal Jatuh Tempo") },
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

            // Recurrence Dropdown
            Text(
                text = "Pengulangan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = when (selectedRecurrence) {
                        "NONE" -> "Tidak Ada"
                        "DAILY" -> "Harian"
                        "WEEKLY" -> "Mingguan"
                        "MONTHLY" -> "Bulanan"
                        else -> "Tidak Ada"
                    },
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showRecurrenceDropdown = true },
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
                    expanded = showRecurrenceDropdown,
                    onDismissRequest = { showRecurrenceDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text = { Text("Tidak Ada") },
                        onClick = {
                            selectedRecurrence = "NONE"
                            showRecurrenceDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Harian") },
                        onClick = {
                            selectedRecurrence = "DAILY"
                            showRecurrenceDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Mingguan") },
                        onClick = {
                            selectedRecurrence = "WEEKLY"
                            showRecurrenceDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Bulanan") },
                        onClick = {
                            selectedRecurrence = "MONTHLY"
                            showRecurrenceDropdown = false
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
                enabled = !reminderState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (name.isNotBlank() && amount.isNotBlank()) {
                        viewModel.createReminder(
                            name = name,
                            dueDate = selectedDate.toString() + "T00:00:00.000Z",
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            recurrence = selectedRecurrence,
                            notes = notes.ifBlank { null }
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
                enabled = !reminderState.isLoading && name.isNotBlank() && amount.isNotBlank()
            ) {
                if (reminderState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Text(
                        text = "Simpan Pengingat",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // ✅ Material3 DatePicker Dialog dengan Calendar UI
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = PrimaryBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal", color = Gray500)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = androidx.compose.material3.DatePickerDefaults.colors(
                    selectedDayContainerColor = PrimaryBlue,
                    todayContentColor = PrimaryBlue,
                    todayDateBorderColor = PrimaryBlue
                )
            )
        }
    }
}