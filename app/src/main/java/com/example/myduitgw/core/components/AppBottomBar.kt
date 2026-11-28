package com.example.myduitgw.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.myduitgw.navigation.Routes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Close
import com.example.myduitgw.ui.theme.Theme


@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigateTab: (String) -> Unit,
    onIncomeClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onTransferClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val menuItems = listOf(
        Triple(
            Icons.Rounded.ArrowUpward,
            Color(0xFFD84343),
            "Expense"
        ),
        Triple(
            Icons.Rounded.ArrowDownward,
            Color(0xFF239653),
            "Income"
        ),
        Triple(
            Icons.Rounded.SwapHoriz,
            Color(0xFF3478D4),
            "Transfer"
        ),
        Triple(
            Icons.Rounded.Image,
            Color(0xFF8950C9),
            "Scan Receipt"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        // QUICK MENU
        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-90).dp)
                .zIndex(3f),
            enter = fadeIn(tween(200)) +
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(200)
                    ),
            exit = fadeOut(tween(150)) +
                    scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(150)
                    )
        ) {
            Surface(
                shape = RoundedCornerShape(40.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    menuItems.forEach { item ->
                        Surface(
                            onClick = {
                                expanded = false

                                when (item.third) {
                                    "Income" -> onIncomeClick()
                                    "Expense" -> onExpenseClick()
                                    "Transfer" -> onTransferClick()
                                }
                            },
                            modifier = Modifier.size(52.dp),
                            shape = CircleShape,
                            color = item.second.copy(alpha = 0.13f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.first,
                                    contentDescription = item.third,
                                    tint = item.second,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // BOTTOM NAVIGATION
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomBarItem(
                    icon = Icons.Rounded.Home,
                    label = "Home",
                    selected = currentRoute == Routes.HOME,
                    onClick = {
                        expanded = false
                        onNavigateTab(Routes.HOME)
                    }
                )

                BottomBarItem(
                    icon = Icons.AutoMirrored.Rounded.List,
                    label = "Transaksi",
                    selected = currentRoute == Routes.TRANSACTIONSHISTORY,
                    onClick = {
                        expanded = false
                        onNavigateTab(Routes.TRANSACTIONSHISTORY)
                    }
                )

                Box(Modifier.size(68.dp))

                BottomBarItem(
                    icon = Icons.Rounded.Savings,
                    label = "Tabungan",
                    selected = currentRoute == Routes.SAVINGS,
                    onClick = {
                        expanded = false
                        onNavigateTab(Routes.SAVINGS)
                    }
                )

                BottomBarItem(
                    icon = Icons.Rounded.Person,
                    label = "Profil",
                    selected = currentRoute == Routes.PROFILE,
                    onClick = {
                        expanded = false
                        onNavigateTab(Routes.PROFILE)
                    }
                )
            }
        }

        // ADD BUTTON
        AddTransactionButton(
            expanded = expanded,
            onClick = {
                expanded = !expanded
            }
        )
    }
}


@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 68.dp, height = 64.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(23.dp),
                tint = if (selected) Theme else Color.Gray
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) Theme else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
private fun AddTransactionButton(
    expanded: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .offset(y = (-24).dp)
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(52.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape
                ),
            shape = CircleShape,
            color = Theme,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (expanded)
                        Icons.Rounded.Close
                    else
                        Icons.Rounded.Add,
                    contentDescription = "Tambah Transaksi",
                    modifier = Modifier.size(26.dp),
                    tint = Color.White
                )
            }
        }
    }
}
