package com.example.myduitgw.ui.transaction


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.ui.theme.Grey50
import com.example.myduitgw.ui.theme.Theme
import com.example.myduitgw.viewmodel.TransferViewModel
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private val Background = Grey50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    onBack: () -> Unit,
    viewModel: TransferViewModel,
) {
    var amount by remember { mutableStateOf("0") }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("0"))
    }

    var description by remember { mutableStateOf("") }

    var isAmountFocused by remember { mutableStateOf(false) }
    var hasAmountInteracted by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getTransactionData("Income")
    }


    var selectedDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val formattedDate = remember(selectedDate) {
        SimpleDateFormat(
            "dd MMMM yyyy",
            Locale("id", "ID")
        ).format(Date(selectedDate))
    }

    var selectedFromAccount by remember { mutableStateOf("") }
    var selectedToAccount by remember { mutableStateOf("") }
    var selectedFromAccountId by remember { mutableStateOf<Int?>(null) }
    var selectedToAccountId by remember { mutableStateOf<Int?>(null) }


    val uiState by viewModel.uiState.collectAsState()

    val categoriesAccount = uiState.categoryAccount?.optJSONObject("data")
    val accounts = categoriesAccount?.optJSONArray("accounts")

    val isAmountZero = amount.toLongOrNull() == null ||
            amount.toLongOrNull() == 0L

    val showAmountError = hasAmountInteracted &&
            isAmountZero &&
            !isAmountFocused

    val isFormValid = amount.toLongOrNull() != null &&
            amount.toLong() > 0 &&
            selectedFromAccount.isNotBlank() &&
            selectedToAccount.isNotBlank() &&
            selectedFromAccount != selectedToAccount

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
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
                text = "Transfer",
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Nominal Transfer",
                        color = if (showAmountError) Color.Red else Color.Black,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rp",
                            color = Color.Black,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(9.dp))

                        BasicTextField(
                            value = textFieldValue,
                            onValueChange = { input ->
                                hasAmountInteracted = true

                                val digits = input.text.filter { it.isDigit() }

                                amount = digits
                                    .trimStart('0')
                                    .ifEmpty { "0" }

                                val formatted = NumberFormat
                                    .getNumberInstance(Locale("id", "ID"))
                                    .format(amount.toLongOrNull() ?: 0L)

                                textFieldValue = TextFieldValue(
                                    text = formatted,
                                    selection = TextRange(formatted.length)
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            cursorBrush = SolidColor(Theme),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    isAmountFocused = it.isFocused
                                }
                        )

                        IconButton(
                            onClick = {
                                hasAmountInteracted = true
                                amount = "0"
                                textFieldValue = TextFieldValue("0")
                            },
                            modifier = Modifier.size(32.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFFF1F3F5),
                                contentColor = Color.Gray
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                if (isAmountFocused) Theme
                                else Color.Transparent
                            )
                    )

                    Text(
                        text = "Masukkan nominal uang yang ingin dipindahkan",
                        color = if (showAmountError) Color.Red else Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            // DETAIL TRANSFER
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
                        text = "Detail Transfer",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    // FROM ACCOUNT
                    Column {
                        FieldLabel("Dari Rekening")

                        DropdownField(
                            value = selectedFromAccount,
                            placeholder = "Pilih rekening",
                            options = accounts,
                            onSelected = { name, id ->
                                selectedFromAccount = name
                                selectedFromAccountId = id
                            },
                            leadingIcon = Icons.Rounded.AccountBalance
                        )
                    }

                    // TO ACCOUNT
                    Column {
                        FieldLabel("Ke Rekening")

                        DropdownField(
                            value = selectedToAccount,
                            placeholder = "Pilih rekening",
                            options = accounts,
                            onSelected = { name, id ->
                                selectedToAccount = name
                                selectedToAccountId = id
                            },
                            leadingIcon = Icons.Rounded.AccountBalance
                        )
                    }


                    // DATE
                    Column {
                        FieldLabel("Tanggal")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Background,
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

                    // DESCRIPTION
                    Column {
                        FieldLabel("Catatan (Opsional)")

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
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

        // TRANSFER BUTTON
        Button(
            onClick = {
                val payload = JSONObject().apply {
                    put("amount", amount.toLong())
                    put("from_account", selectedFromAccount)
                    put("from_accountid", selectedFromAccountId)
                    put("to_account", selectedToAccount)
                    put("to_accountid", selectedToAccountId)
                    put("description", description)
                    put(
                        "transaction_date",
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Date(selectedDate))
                    )
                }

                onBack()
                viewModel.createTransfer(payload)
            },
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
                text = "Transfer Sekarang",
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
                    Text("Batal", color = Theme)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransferAccountDropdown(
    value: String,
    placeholder: String,
    accounts: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    Background,
                    RoundedCornerShape(12.dp)
                )
                .menuAnchor()
                .clickable { expanded = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountBalance,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = value.ifEmpty { placeholder },
                color = if (value.isEmpty()) Color.Gray else Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            accounts.forEach { account ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = account,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        onSelected(account)
                        expanded = false
                    },
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }
        }
    }
}
