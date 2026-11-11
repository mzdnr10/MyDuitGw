package com.example.myduitgw.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.data.repository.FinanceStore
import com.example.myduitgw.data.repository.MockData
import com.example.myduitgw.navigation.Routes
import com.example.myduitgw.ui.components.DualLineChart
import com.example.myduitgw.ui.components.FinanceCard
import com.example.myduitgw.ui.components.ProfileAvatar
import com.example.myduitgw.ui.components.TransactionItem
import com.example.myduitgw.ui.material.FinanceColors
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.util.toCompactRupiah
import com.example.myduitgw.util.toMonthYear
import com.example.myduitgw.util.toRupiah
import java.time.LocalDate
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myduitgw.viewmodel.DashboardViewModel
import org.json.JSONArray
import com.example.myduitgw.data.model.Transaction
import com.example.myduitgw.data.model.TransactionType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import com.example.myduitgw.ui.material.Grey200
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun HomeScreen(
    viewModel: DashboardViewModel,
    store: FinanceStore,
    onNavigate: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.getUserName()
        viewModel.getDashboard()
    }

    val dashboardData = uiState.dashboard?.optJSONObject("data")
    Log.d("data", "$dashboardData")
    val userName = uiState.userName
    val balance = dashboardData?.optLong("balance") ?: 0Lval summary = store.summary()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            color = Grey200
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    ProfileAvatar()

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Hi, $userName",
                            color = Color.Black,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = MockData.userSubtitle,
                            color = Color.Black.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Tidak ada notifikasi baru"
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.Black
                        )
                    }

                    IconButton(
                        onClick = {
                            onNavigate(Routes.PROFILE)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Pengaturan",
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 76.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = Spacing.Normal)
        ) {

            Spacer(modifier = Modifier.height(Spacing.Normal))

            BalanceCard(
                balance = balance,
                userName = userName
            )

            Spacer(modifier = Modifier.height(Spacing.Normal))

            var selectedAccount by remember {
                mutableStateOf("Semua")
            }

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountSelectorItem(
                    accountName = "Semua",
                    accountColor = Theme,
                    icon = Icons.Rounded.Wallet,
                    selected = selectedAccount == "Semua",
                    onClick = {
                        selectedAccount = "Semua"
                    }
                )

                AccountSelectorItem(
                    accountName = "BCA",
                    accountColor = Theme,
                    icon = Icons.Rounded.AccountBalance,
                    selected = selectedAccount == "BCA",
                    onClick = {
                        selectedAccount = "BCA"
                    }
                )

                AccountSelectorItem(
                    accountName = "Mandiri",
                    accountColor = Theme,
                    icon = Icons.Rounded.AccountBalance,
                    selected = selectedAccount == "Mandiri",
                    onClick = {
                        selectedAccount = "Mandiri"
                    }
                )

                AccountSelectorItem(
                    accountName = "DANA",
                    accountColor = Theme,
                    icon = Icons.Rounded.Wallet,
                    selected = selectedAccount == "DANA",
                    onClick = {
                        selectedAccount = "DANA"
                    }
                )

                AccountSelectorItem(
                    accountName = "Shopeepay",
                    accountColor = Theme,
                    icon = Icons.Rounded.AccountBalanceWallet,
                    selected = selectedAccount == "Shopeepay",
                    onClick = {
                        selectedAccount = "Shopeepay"
                    }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Normal))

            // RINGKASAN BULAN INI
            FinanceCard(
                onClick = {
                    onNavigate(Routes.STATISTICS)
                }
            ) {
                Text(
                    text = "Ringkasan Bulan Ini",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(Spacing.Small))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = LocalDate.now()
                            .toMonthYear()
                            .replaceFirstChar { it.titlecase() },
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "Lihat statistik",
                        color = Theme,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.Medium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(86.dp)) {
                        Text(
                            text = "Pemasukan",
                            color = Color.DarkGray,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(Spacing.Medium))

                        Text(
                            text = "Pengeluaran",
                            color = Color.DarkGray,
                            fontSize = 11.sp
                        )
                    }

                    DualLineChart(
                        incomePoints = MockData.incomeTrend,
                        expensePoints = MockData.expenseTrend,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.width(Spacing.Medium))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = summary.monthlyIncome.toCompactRupiah(),
                            color = FinanceColors.Income,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(Spacing.Normal))

                        Text(
                            text = summary.monthlyExpense.toCompactRupiah(),
                            color = Theme,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Large))

            var searchQuery by remember {
                mutableStateOf("")
            }

            val recent_activity =
                dashboardData?.optJSONArray("recent_transactions")

            val recentTransactions = buildList {
                if (recent_activity != null) {
                    for (i in 0 until recent_activity.length()) {
                        val item = recent_activity.getJSONObject(i)

                        add(
                            Transaction(
                                id = item.optString("id"),
                                title = item.optString("title"),
                                amount = item.optString("amount")
                                    .toDoubleOrNull()?.toLong() ?: 0L,
                                type = if (item.optString("type") == "income") {
                                    TransactionType.INCOME
                                } else {
                                    TransactionType.EXPENSE
                                },
                                category = null,
                                date = LocalDate.parse(
                                    item.optString("transaction_date")
                                ),
                                description = item.optString("description")
                            )
                        )
                    }
                }
            }

            val filteredTransactions = recentTransactions.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Transaction",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        IconButton(
                            onClick = {
                                onNavigate(Routes.TRANSACTIONS)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreHoriz,
                                contentDescription = "Lihat semua",
                                tint = Color.Gray
                            )
                        }
                    }

                    // SEARCH
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search transaction",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search",
                                tint = Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFEEEEEE),
                            focusedBorderColor = Theme
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // TRANSACTION LIST
                    if (filteredTransactions.isNotEmpty()) {

                        filteredTransactions.take(5).forEach { transaction ->

                            TransactionItem(
                                transaction = transaction
                            )

                        }

                    } else {
                        Text(
                            text = "Tidak ada transaksi",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(Spacing.Large))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun BalanceCard(
    balance: Long,
    userName: String
) {
    val cardShape = RoundedCornerShape(22.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Theme,
                        Color(0xFF3989F5),
                        Color(0xFF2463C5)
                    )
                )
            )
    ) {

        // Dekorasi background pertama
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 45.dp, y = 25.dp)
                .size(170.dp)
                .rotate(35f)
                .background(
                    Color.White.copy(alpha = 0.12f),
                    RoundedCornerShape(40.dp)
                )
        )

        // Dekorasi background kedua
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-55).dp, y = 65.dp)
                .size(100.dp)
                .rotate(35f)
                .background(
                    Color.White.copy(alpha = 0.10f),
                    RoundedCornerShape(30.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Bagian atas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "MYDUITGW",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "WALLET",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            }

            // Bagian saldo
            Column {
                Text(
                    text = "TOTAL SALDO",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )

                Text(
                    text = balance.toRupiah(),
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bagian bawah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 12.sp
                )

                Text(
                    text = "MY PERSONAL WALLET",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun AccountSelectorItem(
    accountName: String,
    accountColor: Color,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            accountColor
        } else {
            accountColor.copy(alpha = 0.10f)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = accountName,
                modifier = Modifier.size(16.dp),
                tint = if (selected) Color.White else accountColor
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = accountName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else accountColor
            )
        }
    }
}

@Composable
fun RecentTransactionCard(
    transactions: List<Transaction>,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredTransactions = transactions.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transaction",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                IconButton(
                    onClick = onSeeAll
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreHoriz,
                        contentDescription = "Lihat semua transaksi",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // SEARCH
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search transaction",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = Color(0xFF3989F5),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TRANSACTION LIST
            if (filteredTransactions.isEmpty()) {
                Text(
                    text = "Tidak ada transaksi",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                filteredTransactions.take(3).forEach { transaction ->

                    TransactionItem(
                        transaction = transaction
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

