package com.example.myduitgw.ui.screens.home

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
import com.example.myduitgw.data.repository.MockData
import com.example.myduitgw.navigation.Routes
import com.example.myduitgw.ui.components.ProfileAvatar
import com.example.myduitgw.ui.components.TransactionItem
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.theme.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.util.toRupiah
import java.time.LocalDate
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
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
import com.example.myduitgw.viewmodel.HomeViewModel
import com.example.myduitgw.data.model.Transaction
import com.example.myduitgw.data.model.TransactionType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.example.myduitgw.ui.material.Grey200
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.text.style.TextOverflow
import com.example.myduitgw.data.model.DailyExpense

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.getUserName()
        viewModel.getHomeData()
    }

    val homeData = uiState.home?.optJSONObject("data")
    val userName = uiState.userName
    val balanceData = homeData?.optJSONArray("balance")
    val pagerState = rememberPagerState(
        pageCount = { balanceData?.length() ?: 0 }
    )


    val weeklyExpense = uiState.weeklyExpense?.optJSONObject("data")
    val totalExpense = weeklyExpense?.optLong("total_expense") ?: 0L
    val dailyExpensesJson = weeklyExpense?.optJSONArray("daily_expenses")


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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProfileAvatar()

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Hi, $userName",
                            color = Color.Black,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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

                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Pengaturan",
                                tint = Color.Black
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                onClick = {
                                    expanded = false
                                    onNavigate(Routes.PROFILE)
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.Person, contentDescription = null)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    expanded = false
                                    onLogout()
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.Logout, contentDescription = null)
                                }
                            )
                        }
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

            if (uiState.isLoading && homeData == null) {

                HorizontalPager(
                    state = rememberPagerState(pageCount = { 2 }),
                    pageSize = PageSize.Fixed(300.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    pageSpacing = 12.dp
                ) {
                    BalanceCard(
                        accountName = "SEMUA",
                        accountType = "ACCOUNT",
                        balance = 0,
                        userName = userName,
                        accountColor = Color(0xFF216AC5),
                        icon = Icons.Rounded.AccountBalance
                    )
                }


            } else if (balanceData != null && balanceData.length() > 0) {

                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(300.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    pageSpacing = 12.dp
                ) { page ->

                    val account = balanceData.getJSONObject(page)

                    BalanceCard(
                        accountName = account.optString("account_name"),
                        accountType = "ACCOUNT",
                        balance = account.optLong("balance"),
                        userName = userName,
                        accountColor = getAccountColor(
                            account.optInt("account_id")
                        ),
                        icon = if (
                            account.optString("account_name").contains("DANA", true) ||
                            account.optString("account_name").contains("PAY", true)
                        ) Icons.Rounded.Wallet else Icons.Rounded.AccountBalance
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Normal))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (balanceData != null) {
                    for (i in 0 until balanceData.length()) {

                        val account = balanceData.getJSONObject(i)
                        val accountName = account.optString("account_name")

                        AccountSelectorItem(
                            accountName = accountName,
                            accountColor = Theme,
                            icon = if (
                                accountName.contains("DANA", true) ||
                                accountName.contains("PAY", true)
                            ) Icons.Rounded.Wallet else Icons.Rounded.AccountBalance,
                            selected = pagerState.currentPage == i,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(i)
                                }
                            }
                        )
                    }
                } else{
                    AccountSelectorItem(
                        accountName = "SEMUA",
                        accountColor = Theme,
                        icon = Icons.Rounded.AccountBalance,
                        selected = true,
                        onClick = {

                        }
                    )
                    AccountSelectorItem(
                        accountName = "BRI",
                        accountColor = Theme,
                        icon = Icons.Rounded.AccountBalance,
                        selected = false,
                        onClick = {

                        }
                    )

                    AccountSelectorItem(
                        accountName = "MANDIRI",
                        accountColor = Theme,
                        icon = Icons.Rounded.AccountBalance,
                        selected = false,
                        onClick = {

                        }
                    )

                    AccountSelectorItem(
                        accountName = "BCA",
                        accountColor = Theme,
                        icon = Icons.Rounded.AccountBalance,
                        selected = false,
                        onClick = {

                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Normal))

            val dailyExpenses = buildList {

                if (dailyExpensesJson != null && dailyExpensesJson.length() > 0) {

                    for (i in 0 until dailyExpensesJson.length()) {
                        val item = dailyExpensesJson.getJSONObject(i)

                        add(
                            DailyExpense(
                                date = item.getString("date"),
                                day = item.getString("day_name"),
                                expense = item.getLong("expense")
                            )
                        )
                    }

                } else {

                    val today = java.time.LocalDate.now()
                    val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

                    for (i in 6 downTo 0) {
                        val date = today.minusDays(i.toLong())

                        add(
                            DailyExpense(
                                date = date.toString(),
                                day = dayNames[date.dayOfWeek.value - 1],
                                expense = 0L
                            )
                        )
                    }
                }
            }

            WeeklyExpenseCard(
                totalExpense = totalExpense,
                dailyExpenses = dailyExpenses
            )

            Spacer(modifier = Modifier.height(Spacing.Normal))

            var searchQuery by remember {
                mutableStateOf("")
            }

            val recent_activity =
                homeData?.optJSONArray("recent_transactions")

            val recentTransactions = buildList {
                if (recent_activity != null) {
                    for (i in 0 until recent_activity.length()) {
                        val item = recent_activity.getJSONObject(i)

                        add(
                            Transaction(
                                id = item.optString("id"),
                                title = item.optJSONObject("category")?.optString("name") ?: "Unknown",
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
    accountName: String,
    accountType: String,
    balance: Long,
    userName: String,
    accountColor: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accountColor,
                        accountColor.copy(alpha = 0.8f),
                        accountColor.copy(alpha = 0.6f)
                    )
                )
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = accountName.uppercase(),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = icon,
                    contentDescription = accountName,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // BALANCE
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

            // FOOTER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 12.sp
                )

                Text(
                    text = accountType.uppercase(),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp
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
fun WeeklyExpenseCard(
    totalExpense: Long,
    dailyExpenses: List<DailyExpense>,
    modifier: Modifier = Modifier
) {
    val maxExpense = (dailyExpenses.maxOfOrNull { it.expense } ?: 0L)
        .coerceAtLeast(1L)

    val currencyFormat = remember {
        java.text.NumberFormat.getCurrencyInstance(
            java.util.Locale("id", "ID")
        ).apply {
            maximumFractionDigits = 0
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Pengeluaran 7 Hari Terakhir",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Text(
                    text = "Total pengeluaran",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = currencyFormat.format(totalExpense),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Theme
                )
            }

            // Bar Chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                dailyExpenses.forEach { item ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        val barHeight = (
                                item.expense.toFloat() / maxExpense
                                ).coerceIn(0f, 1f)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .fillMaxHeight(
                                        if (item.expense > 0) barHeight else 0.025f
                                    )
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Theme.copy(alpha = 0.85f))
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.day,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

fun getAccountColor(accountId: Int): Color {
    return when (accountId) {
        0 -> Color(0xFF216AC5) // Semua
        1 -> Color(0xFF1E4DB7) // Mandiri
        2 -> Color(0xFF00A86B) // BRI
        3 -> Color(0xFF0084D6) // DANA
        4 -> Color(0xFFEE4D2D) // ShopeePay
        else -> Color(0xFF7952B3)
    }
}

