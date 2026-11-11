package com.example.myduitgw.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.data.model.TransactionType
import com.example.myduitgw.data.repository.FinanceStore
import com.example.myduitgw.data.repository.MockData
import com.example.myduitgw.ui.components.AppFilterChip
import com.example.myduitgw.ui.components.CategoryBars
import com.example.myduitgw.ui.components.ColoredHeader
import com.example.myduitgw.ui.components.DualLineChart
import com.example.myduitgw.ui.components.OverlappingHeaderScreen
import com.example.myduitgw.ui.components.SimpleBarChart
import com.example.myduitgw.ui.material.FinanceColors
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.material.white
import com.example.myduitgw.util.toRupiah

private enum class StatsPeriod {
    WEEK,
    MONTH,
    YEAR
}

//@Composable
//fun StatisticsScreen(
//    store: FinanceStore,
//    onBack: () -> Unit
//) {
//    var period by remember { mutableStateOf(StatsPeriod.MONTH) }
//    val summary = store.summary()
//    val expenses = store.transactions.filter { it.type == TransactionType.EXPENSE }
//    val categoryTotals = expenses
//        .groupBy { it.category }
//        .mapValues { entry -> entry.value.sumOf { it.amount } }
//        .toList()
//        .sortedByDescending { it.second }
//    val maxCategory = (categoryTotals.maxOfOrNull { it.second } ?: 1L).toFloat()
//    val categoryBars = categoryTotals.map { it.first.label to (it.second / maxCategory) }
//
//    val (incomePoints, expensePoints) = when (period) {
//        StatsPeriod.WEEK -> MockData.weeklyIncome to MockData.weeklyExpense
//        StatsPeriod.MONTH -> MockData.incomeTrend to MockData.expenseTrend
//        StatsPeriod.YEAR -> MockData.yearlyIncome to MockData.yearlyExpense
//    }
//
//    OverlappingHeaderScreen(
//        headerHeight = 180.dp,
//        overlap = 48.dp,
//        header = {
//            ColoredHeader(height = 180.dp) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
//                            contentDescription = "Kembali",
//                            tint = white
//                        )
//                    }
//                    Text(
//                        text = "Statistik",
//                        color = white,
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//                Spacer(modifier = Modifier.height(Spacing.Medium))
//                Text(
//                    text = "Pantau tren keuanganmu",
//                    color = white.copy(alpha = 0.8f),
//                    fontSize = 13.sp
//                )
//            }
//        }
//    ) {
//        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
//            AppFilterChip("Minggu", period == StatsPeriod.WEEK) { period = StatsPeriod.WEEK }
//            AppFilterChip("Bulan", period == StatsPeriod.MONTH) { period = StatsPeriod.MONTH }
//            AppFilterChip("Tahun", period == StatsPeriod.YEAR) { period = StatsPeriod.YEAR }
//        }
//        Spacer(modifier = Modifier.height(Spacing.Large))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column {
//                Text(text = "Pemasukan", color = Color.Gray, fontSize = 12.sp)
//                Spacer(modifier = Modifier.height(Spacing.Small))
//                Text(
//                    text = summary.monthlyIncome.toRupiah(),
//                    color = FinanceColors.Income,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//            Column {
//                Text(text = "Pengeluaran", color = Color.Gray, fontSize = 12.sp)
//                Spacer(modifier = Modifier.height(Spacing.Small))
//                Text(
//                    text = summary.monthlyExpense.toRupiah(),
//                    color = FinanceColors.Expense,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//        Spacer(modifier = Modifier.height(Spacing.Large))
//        Text(
//            text = "Tren saldo",
//            fontSize = 15.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(Spacing.Medium))
//        DualLineChart(
//            incomePoints = incomePoints,
//            expensePoints = expensePoints,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(140.dp)
//        )
//        Spacer(modifier = Modifier.height(Spacing.Large))
//        Text(
//            text = "Pengeluaran",
//            fontSize = 15.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(Spacing.Medium))
//        SimpleBarChart(
//            values = expensePoints,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(120.dp),
//            color = Theme
//        )
//        Spacer(modifier = Modifier.height(Spacing.Large))
//        Text(
//            text = "Kategori pengeluaran",
//            fontSize = 15.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(Spacing.Medium))
//        categoryTotals.forEach { (category, amount) ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = category.label, fontSize = 13.sp, color = Color.DarkGray)
//                Text(text = amount.toRupiah(), fontSize = 13.sp, color = Color.Black)
//            }
//            Spacer(modifier = Modifier.height(Spacing.Small))
//        }
//        Spacer(modifier = Modifier.height(Spacing.Medium))
//        CategoryBars(
//            items = categoryBars,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height((categoryBars.size * 28).dp.coerceAtLeast(80.dp))
//        )
//        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
//    }
//}
