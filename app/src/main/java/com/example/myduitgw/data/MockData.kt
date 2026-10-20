package com.example.myduitgw.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.myduitgw.model.Category
import com.example.myduitgw.model.FinancialSummary
import com.example.myduitgw.model.SavingsGoal
import com.example.myduitgw.model.Transaction
import com.example.myduitgw.model.TransactionType
import java.time.LocalDate
import java.util.UUID

object MockData {
    val userName: String = "Zidan"
    val userSubtitle: String = "Welcome Back Again"

    val incomeTrend: List<Float> = listOf(0.45f, 0.52f, 0.48f, 0.62f, 0.70f, 0.66f)
    val expenseTrend: List<Float> = listOf(0.72f, 0.64f, 0.70f, 0.58f, 0.66f, 0.50f)

    val weeklyIncome: List<Float> = listOf(0.4f, 0.55f, 0.35f, 0.7f, 0.5f, 0.8f, 0.6f)
    val weeklyExpense: List<Float> = listOf(0.5f, 0.4f, 0.6f, 0.45f, 0.7f, 0.55f, 0.35f)

    val yearlyIncome: List<Float> = listOf(0.5f, 0.48f, 0.55f, 0.6f, 0.58f, 0.7f, 0.65f, 0.72f, 0.68f, 0.74f, 0.7f, 0.8f)
    val yearlyExpense: List<Float> = listOf(0.45f, 0.5f, 0.48f, 0.52f, 0.6f, 0.55f, 0.62f, 0.58f, 0.5f, 0.57f, 0.54f, 0.6f)

    fun transactions(): List<Transaction> {
        val today = LocalDate.now()
        return listOf(
            Transaction(
                id = "t1",
                title = "Gaji Agustus",
                amount = 8_500_000,
                type = TransactionType.INCOME,
                category = Category.SALARY,
                date = today,
                description = "Gaji bulanan"
            ),
            Transaction(
                id = "t2",
                title = "Kopi Janji Jiwa",
                amount = 28_000,
                type = TransactionType.EXPENSE,
                category = Category.FOOD,
                date = today,
                description = "Ngopi pagi"
            ),
            Transaction(
                id = "t3",
                title = "GoFood - Bakso",
                amount = 55_000,
                type = TransactionType.EXPENSE,
                category = Category.FOOD,
                date = today.minusDays(1)
            ),
            Transaction(
                id = "t4",
                title = "Transfer ke Budi",
                amount = 250_000,
                type = TransactionType.EXPENSE,
                category = Category.OTHER_EXPENSE,
                date = today.minusDays(1)
            ),
            Transaction(
                id = "t5",
                title = "Proyek Freelance UI",
                amount = 1_500_000,
                type = TransactionType.INCOME,
                category = Category.FREELANCE,
                date = today.minusDays(2)
            ),
            Transaction(
                id = "t6",
                title = "Belanja Bulanan",
                amount = 420_000,
                type = TransactionType.EXPENSE,
                category = Category.GROCERIES,
                date = today.minusDays(3)
            ),
            Transaction(
                id = "t7",
                title = "Grab ke Kantor",
                amount = 32_000,
                type = TransactionType.EXPENSE,
                category = Category.TRANSPORT,
                date = today.minusDays(3)
            ),
            Transaction(
                id = "t8",
                title = "Token Listrik",
                amount = 150_000,
                type = TransactionType.EXPENSE,
                category = Category.BILLS,
                date = today.minusDays(5)
            ),
            Transaction(
                id = "t9",
                title = "Netflix",
                amount = 54_000,
                type = TransactionType.EXPENSE,
                category = Category.ENTERTAINMENT,
                date = today.minusDays(6)
            ),
            Transaction(
                id = "t10",
                title = "Setor Dana Darurat",
                amount = 500_000,
                type = TransactionType.EXPENSE,
                category = Category.SAVINGS,
                date = today.minusDays(7)
            ),
            Transaction(
                id = "t11",
                title = "Baju Kerja",
                amount = 275_000,
                type = TransactionType.EXPENSE,
                category = Category.SHOPPING,
                date = LocalDate.of(2026, 7, 18)
            ),
            Transaction(
                id = "t12",
                title = "Bonus Proyek",
                amount = 750_000,
                type = TransactionType.INCOME,
                category = Category.FREELANCE,
                date = LocalDate.of(2026, 7, 12)
            )
        )
    }

    fun savingsGoals(): List<SavingsGoal> = listOf(
        SavingsGoal(
            id = "s1",
            name = "Dana Darurat",
            targetAmount = 10_000_000,
            currentAmount = 6_500_000
        ),
        SavingsGoal(
            id = "s2",
            name = "Liburan Yogyakarta",
            targetAmount = 4_000_000,
            currentAmount = 1_800_000
        ),
        SavingsGoal(
            id = "s3",
            name = "Laptop Baru",
            targetAmount = 15_000_000,
            currentAmount = 3_250_000
        )
    )
}

class FinanceStore {
    val transactions: SnapshotStateList<Transaction> = mutableStateListOf<Transaction>().apply {
        addAll(MockData.transactions())
    }

    val savingsGoals: SnapshotStateList<SavingsGoal> = mutableStateListOf<SavingsGoal>().apply {
        addAll(MockData.savingsGoals())
    }

    fun summary(): FinancialSummary {
        val now = LocalDate.now()
        val monthStart = now.withDayOfMonth(1)
        val monthly = transactions.filter { it.date >= monthStart }
        val income = monthly.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expense = monthly.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val balance = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount } -
            transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        return FinancialSummary(
            totalBalance = balance.coerceAtLeast(0L),
            weeklyGrowthPercent = 1.2f,
            monthlyIncome = income,
            monthlyExpense = expense
        )
    }

    fun addTransaction(
        title: String,
        amount: Long,
        type: TransactionType,
        category: Category,
        date: LocalDate,
        description: String
    ) {
        transactions.add(
            0,
            Transaction(
                id = UUID.randomUUID().toString(),
                title = title.ifBlank { category.label },
                amount = amount,
                type = type,
                category = category,
                date = date,
                description = description
            )
        )
    }

    fun addSavingsGoal(name: String, targetAmount: Long, currentAmount: Long) {
        savingsGoals.add(
            0,
            SavingsGoal(
                id = UUID.randomUUID().toString(),
                name = name,
                targetAmount = targetAmount,
                currentAmount = currentAmount
            )
        )
    }
}
