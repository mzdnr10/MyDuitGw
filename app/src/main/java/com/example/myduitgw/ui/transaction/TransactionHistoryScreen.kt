package com.example.myduitgw.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.ui.theme.FinanceColors
import com.example.myduitgw.ui.theme.Grey50
import com.example.myduitgw.ui.theme.Theme
import com.example.myduitgw.util.toDisplayDate
import com.example.myduitgw.util.toSignedRupiah

data class HistoryTransactionUiModel(
    val id: Int,
    val categoryName: String,
    val accountName: String,
    val description: String?,
    val amount: Long,
    val type: String,
    val transactionDate: String
)

private val HistoryBackground = Grey50
private val HistoryBlue = Theme
private val IncomeGreen = FinanceColors.Income
private val ExpenseRed = FinanceColors.Expense

@Composable
fun TransactionHistoryScreen(
    transactions: List<HistoryTransactionUiModel>,
    onTransactionClick: (HistoryTransactionUiModel) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTransactions = transactions.filter { transaction ->
        val matchesFilter = when (selectedFilter) {
            "Income" -> transaction.type.equals("income", true)
            "Expense" -> transaction.type.equals("expense", true)
            else -> true
        }

        val matchesSearch =
            transaction.categoryName.contains(searchQuery, ignoreCase = true) ||
                    transaction.accountName.contains(searchQuery, ignoreCase = true) ||
                    transaction.description.orEmpty().contains(searchQuery, ignoreCase = true)

        matchesFilter && matchesSearch
    }

    val groupedTransactions = filteredTransactions.groupBy {
        it.transactionDate.take(10)
    }.toSortedMap(compareByDescending { it })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HistoryBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Transactions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Text(
            text = "Manage your transaction history",
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color(0xFF374151),
                        fontSize = 14.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search transactions...",
                                color = Color(0xFF9CA3AF),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Income", "Expense").forEach { filter ->
                val isSelected = selectedFilter == filter

                Surface(
                    modifier = Modifier.weight(1f),
                    onClick = { selectedFilter = filter },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) HistoryBlue else Color.White
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White
                            else Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = Color(0xFFB0B7C3)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "No transactions found",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Text(
                        text = "Your transactions will appear here",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                groupedTransactions.forEach { (date, items) ->
                    item(key = "date_$date") {
                        Text(
                            text = date.toDisplayDate(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(
                                top = 8.dp,
                                bottom = 2.dp
                            )
                        )
                    }

                    items(
                        items = items,
                        key = { it.id }
                    ) { transaction ->
                        TransactionHistoryItem(
                            transaction = transaction,
                            onClick = {
                                onTransactionClick(transaction)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionHistoryItem(
    transaction: HistoryTransactionUiModel,
    onClick: () -> Unit
) {
    val isIncome = transaction.type.equals("income", true)

    val type = if (isIncome) {
        TransactionType.INCOME
    } else {
        TransactionType.EXPENSE
    }

    val amountColor = if (isIncome) IncomeGreen else ExpenseRed

    val icon = when (transaction.categoryName.lowercase()) {
        "food", "makanan", "food & beverage" ->
            Icons.Default.Restaurant

        "shopping", "belanja" ->
            Icons.Default.ShoppingBag

        "transport", "transportasi" ->
            Icons.Default.DirectionsCar

        else ->
            Icons.Default.Payments
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = if (isIncome) Color(0xFFE8F7EE)
                        else Color(0xFFF1F3F5),
                        shape = RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = transaction.categoryName,
                    tint = if (isIncome) IncomeGreen
                    else Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = transaction.categoryName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    maxLines = 1
                )

                Text(
                    text = transaction.description
                        ?.takeIf { it.isNotBlank() }
                        ?: transaction.accountName,
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    maxLines = 1
                )

                if (!transaction.description.isNullOrBlank()) {
                    Text(
                        text = transaction.accountName,
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = transaction.amount.toSignedRupiah(type),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = amountColor
                )

                Text(
                    text = transaction.transactionDate
                        .takeIf { it.length >= 10 }
                        ?.substring(0, 10)
                        ?.toDisplayDate()
                        ?: transaction.transactionDate,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}