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
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SwapHoriz
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
import com.example.myduitgw.ui.components.QuickActionButton
import com.example.myduitgw.ui.components.SectionHeader
import com.example.myduitgw.ui.components.TransactionItem
import com.example.myduitgw.ui.material.FinanceColors
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.material.Radius
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.util.toCompactRupiah
import com.example.myduitgw.util.toMonthYear
import com.example.myduitgw.util.toRupiah
import java.time.LocalDate
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myduitgw.viewmodel.DashboardViewModel

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
    val userData = dashboardData?.optJSONObject("user_data")
    val balance = dashboardData?.optLong("balance") ?: 0L
    val income = dashboardData?.optLong("income") ?: 0L
    val expense = dashboardData?.optLong("expense") ?: 0L
    val weekly_growth_percent = dashboardData?.optLong("weekly_growth_percent") ?: 0L
    val recent_activity = dashboardData?.optLong("weekly_growth_percent") ?: "Tidak ada transaksi"

    val summary = store.summary()
    val recent = store.transactions.take(3)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            color = Theme,
            shape = RoundedCornerShape(
                bottomStart = 28.dp,
                bottomEnd = 28.dp
            )
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.Normal)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Large),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar()
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Hi, $userName",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = MockData.userSubtitle,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                }
                Row {
                    IconButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Tidak ada notifikasi baru")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onNavigate(Routes.PROFILE) }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Pengaturan",
                            tint = Color.White
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(Spacing.Large))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(Radius.Large),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.Normal, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Total Saldo", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = balance.toRupiah(),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "$weekly_growth_percent% minggu ini",
                            color = if (weekly_growth_percent > 0) {
                                FinanceColors.Income
                            } else {
                                FinanceColors.Expense
                            },
                            fontSize = 11.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionButton(
                            icon = Icons.Rounded.AccountBalanceWallet,
                            label = "Isi Saldo",
                            onClick = { onNavigate(Routes.ADD_TRANSACTION) }
                        )
                        QuickActionButton(
                            icon = Icons.Rounded.SwapHoriz,
                            label = "Transfer",
                            onClick = { onNavigate(Routes.ADD_TRANSACTION) }
                        )
                        QuickActionButton(
                            icon = Icons.Rounded.Payment,
                            label = "Bayar",
                            onClick = { onNavigate(Routes.ADD_TRANSACTION) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Normal))

            FinanceCard(onClick = { onNavigate(Routes.STATISTICS) }) {
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
                        text = LocalDate.now().toMonthYear().replaceFirstChar { it.titlecase() },
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
                        Text(text = "Pemasukan", color = Color.DarkGray, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(Spacing.Medium))
                        Text(text = "Pengeluaran", color = Color.DarkGray, fontSize = 11.sp)
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

            SectionHeader(
                title = "Transaksi Terakhir",
                actionText = "Lihat Selengkapnya",
                onAction = { onNavigate(Routes.TRANSACTIONS) }
            )
            Spacer(modifier = Modifier.height(Spacing.Medium))
            recent.forEach { transaction ->
                TransactionItem(transaction = transaction)
            }
            Spacer(modifier = Modifier.height(Spacing.Large))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
