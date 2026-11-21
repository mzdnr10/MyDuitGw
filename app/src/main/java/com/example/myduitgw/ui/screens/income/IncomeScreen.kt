
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
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.viewmodel.IncomeViewModel
import org.json.JSONArray
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val IncomeBackground = Color(0xFFF5F6F8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    viewModel: IncomeViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
){

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getIncomeData()
    }
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

    val categoriesAccount = uiState.categoryAccount?.optJSONObject("data")
    Log.d("pppp", "$categoriesAccount")

    val categories = categoriesAccount?.optJSONArray("categories")

    val accounts = categoriesAccount?.optJSONArray("accounts")


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

    var isAmountFocused by remember { mutableStateOf(false) }
    var hasAmountInteracted by remember { mutableStateOf(false) }

    val isAmountZero = amount.toLongOrNull() == null ||
            amount.toLongOrNull() == 0L

    val showAmountError = hasAmountInteracted &&
            isAmountZero &&
            !isAmountFocused

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Nominal",
                        color = if (showAmountError) Color.Red else Color.Black,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rp",
                                color = Color.Black,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            var textFieldValue by remember {
                                mutableStateOf(TextFieldValue("0"))
                            }

                            Spacer(modifier = Modifier.width(9.dp))

                            BasicTextField(
                                value = textFieldValue,
                                onValueChange = { input ->
                                    hasAmountInteracted = true

                                    val digits = input.text.filter { it.isDigit() }

                                    amount = digits
                                        .trimStart('0')
                                        .ifEmpty { "0" }

                                    val formatted = NumberFormat.getNumberInstance(Locale("id", "ID"))
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
                                    textFieldValue = TextFieldValue(
                                        text = "0",
                                        selection = TextRange(1)
                                    )
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
                    }

                    // Garis bawah input
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Masukkan nominal uang yang diterima",
                        color = if (showAmountError) Color.Red else Color.Black,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
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


                    Column {
                        FieldLabel("Kategori")

                        DropdownField(
                            value = selectedCategory,
                            placeholder = "Pilih kategori",
                            options = categories,
                            onSelected = { selectedCategory = it },
                            leadingIcon = Icons.Rounded.Category
                        )
                    }

                    // ACCOUNT
                    Column {
                        FieldLabel("Masuk ke rekening")

                        DropdownField(
                            value = selectedAccount,
                            placeholder = "Pilih rekening",
                            options = accounts,
                            onSelected = { selectedAccount = it },
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    value: String,
    placeholder: String,
    options: JSONArray?,
    onSelected: (String) -> Unit,
    leadingIcon: ImageVector? = null
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
                    IncomeBackground,
                    RoundedCornerShape(12.dp)
                )
                .menuAnchor()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

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
            options?.let { array ->
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    val itemName = item.getString("name")

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = itemName,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            onSelected(itemName)
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
}
