package com.example.myduitgw.ui.transaction

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
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
import com.example.myduitgw.data.model.TransactionType
import com.example.myduitgw.data.repository.FinanceStore
import com.example.myduitgw.ui.components.AppFilterChip
import com.example.myduitgw.ui.components.ColoredHeader
import com.example.myduitgw.ui.components.OverlappingHeaderScreen
import com.example.myduitgw.ui.components.TransactionItem
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.white
import com.example.myduitgw.util.toGroupLabel
import com.example.myduitgw.util.toRupiah
import java.time.LocalDate

private enum class TransactionFilter {
    ALL,
    INCOME,
    EXPENSE,
    THIS_MONTH
}

@Composable
fun TransactionsScreen(store: FinanceStore) {
    var filter by remember { mutableStateOf(TransactionFilter.ALL) }
    val today = LocalDate.now()
    val monthStart = today.withDayOfMonth(1)

    val filtered = store.transactions.filter { transaction ->
        when (filter) {
            TransactionFilter.ALL -> true
            TransactionFilter.INCOME -> transaction.type == TransactionType.INCOME
            TransactionFilter.EXPENSE -> transaction.type == TransactionType.EXPENSE
            TransactionFilter.THIS_MONTH -> transaction.date >= monthStart
        }
    }

    val incomeTotal = filtered.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expenseTotal = filtered.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val grouped = filtered.groupBy { it.date.toGroupLabel() }

    OverlappingHeaderScreen(
        headerHeight = 240.dp,
        overlap = 56.dp,
        header = {
            ColoredHeader(height = 240.dp) {
                Text(
                    text = "Transaksi",
                    color = white,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Spacing.Small))
                Text(
                    text = "${filtered.size} transaksi tercatat",
                    color = white.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(Spacing.Large))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Pemasukan", color = white.copy(alpha = 0.75f), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(Spacing.Small))
                        Text(
                            text = incomeTotal.toRupiah(),
                            color = white,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text(text = "Pengeluaran", color = white.copy(alpha = 0.75f), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(Spacing.Small))
                        Text(
                            text = expenseTotal.toRupiah(),
                            color = white,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) {
        Text(
            text = "Riwayat Transaksi",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(Spacing.Normal))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AppFilterChip("Semua", filter == TransactionFilter.ALL) { filter = TransactionFilter.ALL }
            AppFilterChip("Pemasukan", filter == TransactionFilter.INCOME) { filter = TransactionFilter.INCOME }
            AppFilterChip("Pengeluaran", filter == TransactionFilter.EXPENSE) { filter = TransactionFilter.EXPENSE }
            AppFilterChip("Bulan Ini", filter == TransactionFilter.THIS_MONTH) { filter = TransactionFilter.THIS_MONTH }
        }
        Spacer(modifier = Modifier.height(Spacing.Large))
        if (grouped.isEmpty()) {
            Text(
                text = "Belum ada transaksi pada filter ini",
                color = Color.Gray,
                fontSize = 13.sp
            )
        } else {
            grouped.forEach { (label, items) ->
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(Spacing.Medium))
                items.forEach { transaction ->
                    TransactionItem(transaction = transaction)
                }
                Spacer(modifier = Modifier.height(Spacing.Normal))
            }
        }
        Spacer(modifier = Modifier.height(Spacing.Large))
    }
}
