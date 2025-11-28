package com.alduraimron.accountinggrow.presentation.reminder

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alduraimron.accountinggrow.domain.model.Reminder
import com.alduraimron.accountinggrow.ui.theme.ExpenseRed
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.IncomeGreen
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    onBack: () -> Unit,
    onAddReminder: () -> Unit,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val reminderState by viewModel.reminderState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }
    var filterPaid by remember { mutableStateOf<Boolean?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pengingat Pembayaran") },
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
                    onClick = onAddReminder,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Tambah Pengingat")
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
                        selected = filterPaid == null,
                        onClick = {
                            filterPaid = null
                            viewModel.loadReminders(null)
                        },
                        label = { Text("Semua") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = filterPaid == false,
                        onClick = {
                            filterPaid = false
                            viewModel.loadReminders(false)
                        },
                        label = { Text("Belum Lunas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ExpenseRed,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = filterPaid == true,
                        onClick = {
                            filterPaid = true
                            viewModel.loadReminders(true)
                        },
                        label = { Text("Lunas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IncomeGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                if (reminderState.isLoading && reminderState.reminders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                } else if (reminderState.reminders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Gray500
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Belum ada pengingat",
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(reminderState.reminders) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                onMarkPaid = { viewModel.markAsPaid(reminder.id) },
                                onDelete = {
                                    reminderToDelete = reminder
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

        // Delete Dialog
        if (showDeleteDialog && reminderToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Hapus Pengingat") },
                text = { Text("Apakah Anda yakin ingin menghapus pengingat ${reminderToDelete?.name}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteReminder(reminderToDelete!!.id)
                            showDeleteDialog = false
                            reminderToDelete = null
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

        // Success/Error Messages
        reminderState.successMessage?.let { message ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = IncomeGreen,
                contentColor = Color.White
            ) {
                Text(text = message)
            }
        }

        reminderState.errorMessage?.let { error ->
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
fun ReminderCard(
    reminder: Reminder,
    onMarkPaid: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isPaid) Color.White.copy(alpha = 0.7f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (reminder.isPaid) IncomeGreen.copy(alpha = 0.1f)
                        else ExpenseRed.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (reminder.isPaid) Icons.Default.CheckCircle else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (reminder.isPaid) IncomeGreen else ExpenseRed,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = formatCurrency(reminder.amount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ExpenseRed
                )
                Text(
                    text = "Jatuh tempo: ${formatDate(reminder.dueDate)}",
                    fontSize = 12.sp,
                    color = Gray500
                )
                if (!reminder.notes.isNullOrBlank()) {
                    Text(
                        text = reminder.notes,
                        fontSize = 11.sp,
                        color = Gray500,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Actions
            Column(horizontalAlignment = Alignment.End) {
                if (!reminder.isPaid) {
                    IconButton(
                        onClick = onMarkPaid,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mark Paid",
                            tint = IncomeGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ExpenseRed,
                        modifier = Modifier.size(20.dp)
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