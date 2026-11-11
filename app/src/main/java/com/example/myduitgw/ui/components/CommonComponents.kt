package com.example.myduitgw.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.data.model.Transaction
import com.example.myduitgw.data.model.TransactionType
import com.example.myduitgw.ui.material.FinanceColors
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.material.Radius
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.material.white
import com.example.myduitgw.ui.material.black
import com.example.myduitgw.util.toDisplayDate
import com.example.myduitgw.util.toSignedRupiah
import com.example.myduitgw.data.model.Category
import com.example.myduitgw.ui.material.Grey800

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(black.copy(alpha = 0.22f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Z",
            color = white,
            fontSize = (size.value * 0.4f).sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                color = Theme,
                fontSize = 12.sp,
                modifier = Modifier.clickable(onClick = onAction)
            )
        }
    }
}

@Composable
fun FinanceCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(Radius.Large)
    if (onClick != null) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = white,
            shadowElevation = 4.dp,
            onClick = onClick,
            content = { Column(Modifier.padding(Spacing.Normal), content = content) }
        )
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = white,
            shadowElevation = 4.dp,
            content = { Column(Modifier.padding(Spacing.Normal), content = content) }
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Grey50,
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    tint = Theme
                )
            }
        }
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun AppFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) Theme else Grey50,
        onClick = onClick
    ) {
        Text(
            text = label,
            color = if (selected) white else Color.DarkGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = Spacing.Normal, vertical = Spacing.Medium)
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    showDate: Boolean = true
) {
    val category = transaction.category ?: if (
        transaction.type == TransactionType.INCOME
    ) {
        Category.OTHER_INCOME
    } else {
        Category.OTHER_EXPENSE
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Grey50
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.label,
                    modifier = Modifier.size(20.dp),
                    tint = Theme
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                color = Color.DarkGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (showDate) {
                    "${category.label} • ${transaction.date.toDisplayDate()}"
                } else {
                    category.label
                },
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
        Text(
            text = transaction.amount.toSignedRupiah(transaction.type),
            color = if (transaction.type == TransactionType.INCOME) {
                FinanceColors.Income
            } else {
                FinanceColors.Expense
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun OverlappingHeaderScreen(
    headerHeight: Dp = 180.dp,
    overlap: Dp = 48.dp,
    header: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {
        header()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(headerHeight - overlap))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 520.dp),
                shape = RoundedCornerShape(
                    topStart = Radius.Sheet,
                    topEnd = Radius.Sheet
                ),
                color = white
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.Large, vertical = Spacing.Large),
                    content = content
                )
            }
        }
    }
}

@Composable
fun ColoredHeader(
    height: Dp = 180.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Theme)
            .padding(horizontal = Spacing.Large, vertical = Spacing.Large),
        content = content
    )
}
