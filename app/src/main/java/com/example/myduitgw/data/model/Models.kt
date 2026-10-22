package com.example.myduitgw.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class Category(
    val label: String,
    val type: TransactionType
) {
    SALARY("Gaji", TransactionType.INCOME),
    FREELANCE("Freelance", TransactionType.INCOME),
    OTHER_INCOME("Pemasukan Lain", TransactionType.INCOME),
    FOOD("Makanan", TransactionType.EXPENSE),
    GROCERIES("Belanja", TransactionType.EXPENSE),
    TRANSPORT("Transportasi", TransactionType.EXPENSE),
    BILLS("Tagihan", TransactionType.EXPENSE),
    ENTERTAINMENT("Hiburan", TransactionType.EXPENSE),
    SHOPPING("Belanja Barang", TransactionType.EXPENSE),
    SAVINGS("Tabungan", TransactionType.EXPENSE),
    OTHER_EXPENSE("Pengeluaran Lain", TransactionType.EXPENSE);

    val icon: ImageVector
        get() = when (this) {
            SALARY, FREELANCE, OTHER_INCOME -> Icons.Rounded.Work
            FOOD -> Icons.Rounded.Restaurant
            GROCERIES -> Icons.Rounded.LocalGroceryStore
            TRANSPORT -> Icons.Rounded.DirectionsBus
            BILLS -> Icons.Rounded.Bolt
            ENTERTAINMENT -> Icons.Rounded.Movie
            SHOPPING -> Icons.Rounded.ShoppingBag
            SAVINGS -> Icons.Rounded.Savings
            OTHER_EXPENSE -> Icons.Rounded.Coffee
        }
}

data class Transaction(
    val id: String,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val category: Category,
    val date: LocalDate,
    val description: String = ""
)

data class SavingsGoal(
    val id: String,
    val name: String,
    val targetAmount: Long,
    val currentAmount: Long
) {
    val progress: Float
        get() = if (targetAmount <= 0L) {
            0f
        } else {
            (currentAmount.toFloat() / targetAmount.toFloat()).coerceIn(0f, 1f)
        }
}

data class FinancialSummary(
    val totalBalance: Long,
    val weeklyGrowthPercent: Float,
    val monthlyIncome: Long,
    val monthlyExpense: Long
)
