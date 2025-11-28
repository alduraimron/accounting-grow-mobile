package com.alduraimron.accountinggrow.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alduraimron.accountinggrow.domain.model.TransactionType
import com.alduraimron.accountinggrow.ui.theme.ExpenseRed
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.IncomeGreen
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, type: String) -> Unit,
    isLoading: Boolean = false
) {
    var categoryName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Kategori",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Category Name
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Nama Kategori") },
                    placeholder = { Text("Contoh: Gaji, Makanan, Transport") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Gray500.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryBlue,
                        cursorColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Type Selection
                Text(
                    text = "Tipe Kategori",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Income
                    TypeButton(
                        text = "Pemasukan",
                        isSelected = selectedType == TransactionType.INCOME,
                        color = IncomeGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedType = TransactionType.INCOME },
                        enabled = !isLoading
                    )

                    // Expense
                    TypeButton(
                        text = "Pengeluaran",
                        isSelected = selectedType == TransactionType.EXPENSE,
                        color = ExpenseRed,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedType = TransactionType.EXPENSE },
                        enabled = !isLoading
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCreate(
                        categoryName,
                        if (selectedType == TransactionType.INCOME) "INCOME" else "EXPENSE"
                    )
                },
                enabled = categoryName.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                } else {
                    Text("Simpan")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun TypeButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = modifier
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center
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