
package com.example.myduitgw.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.ui.material.Theme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val IncomeBackground = Color(0xFFF5F6F8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
){
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedAccount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var selectedDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val categories = listOf(
        "Gaji",
        "Bonus",
        "Freelance",
        "Bisnis",
        "Investasi",
        "Hadiah",
        "Lainnya"
    )

    val accounts = listOf(
        "Mandiri",
        "BRI",
        "Cash"
    )

    val formattedAmount = remember(amount) {
        if (amount.isBlank()) {
            ""
        } else {
            NumberFormat.getNumberInstance(Locale("id", "ID"))
                .format(amount.toLongOrNull() ?: 0L)
        }
    }

    val formattedDate = remember(selectedDate) {
        SimpleDateFormat(
            "dd MMMM yyyy",
            Locale("id", "ID")
        ).format(Date(selectedDate))
    }

    val isFormValid = amount.isNotBlank() &&
            amount.toLongOrNull() != null &&
            amount.toLongOrNull()!! > 0 &&
            selectedCategory.isNotBlank() &&
            selectedAccount.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IncomeBackground)
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Tambah Pemasukan",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // AMOUNT
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Jumlah Pemasukan",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rp",
                            color = Theme,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = formattedAmount,
                            onValueChange = { input ->
                                amount = input.filter { it.isDigit() }
                            },
                            placeholder = {
                                Text(
                                    "0",
                                    color = Color.LightGray
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Theme
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.widthIn(max = 250.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Masukkan nominal uang yang diterima",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            // DETAIL TRANSAKSI
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Detail Transaksi",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    // CATEGORY
                    Column {
                        FieldLabel("Kategori")

                        DropdownField(
                            value = selectedCategory,
                            placeholder = "Pilih kategori pemasukan",
                            options = categories,
                            onSelected = { selectedCategory = it }
                        )
                    }

                    // ACCOUNT
                    Column {
                        FieldLabel("Masuk ke rekening")

                        DropdownField(
                            value = selectedAccount,
                            placeholder = "Pilih rekening",
                            options = accounts,
                            onSelected = { selectedAccount = it }
                        )
                    }

                    // DATE
                    Column {
                        FieldLabel("Tanggal")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    IncomeBackground,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    showDatePicker = true
                                }
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarMonth,
                                contentDescription = null,
                                tint = Color.Gray
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = formattedDate,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // NOTE
                    Column {
                        FieldLabel("Catatan (Opsional)")

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = {
                                Text("Tambahkan catatan")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Theme,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        }

        // SAVE BUTTON
        Button(
            onClick = onSave,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme,
                disabledContainerColor = Theme.copy(alpha = 0.4f)
            )
        ) {
            Text(
                text = "Simpan Pemasukan",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // DATE PICKER
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Pilih", color = Theme)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}


@Composable
private fun DropdownField(
    value: String,
    placeholder: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    IncomeBackground,
                    RoundedCornerShape(12.dp)
                )
                .clickable {
                    expanded = true
                }
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Wallet,
                contentDescription = null,
                tint = Theme
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = value.ifBlank { placeholder },
                color = if (value.isBlank()) Color.Gray else Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
