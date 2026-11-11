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
import com.example.myduitgw.ui.material.Theme

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigateTab: (String) -> Unit,
    onAddTransaction: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            color = Color.White,
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
                    onClick = { onNavigateTab(Routes.HOME) }
                )
                BottomBarItem(
                    icon = Icons.AutoMirrored.Rounded.List,
                    label = "Transaksi",
                    selected = currentRoute == Routes.TRANSACTIONS,
                    onClick = { onNavigateTab(Routes.TRANSACTIONS) }
                )
                Box(Modifier.size(68.dp))
                BottomBarItem(
                    icon = Icons.Rounded.Savings,
                    label = "Tabungan",
                    selected = currentRoute == Routes.SAVINGS,
                    onClick = { onNavigateTab(Routes.SAVINGS) }
                )
                BottomBarItem(
                    icon = Icons.Rounded.Person,
                    label = "Profil",
                    selected = currentRoute == Routes.PROFILE,
                    onClick = { onNavigateTab(Routes.PROFILE) }
                )
            }
        }

        AddTransactionButton(onClick = onAddTransaction)
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
private fun AddTransactionButton(onClick: () -> Unit) {
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
                .shadow(elevation = 8.dp, shape = CircleShape),
            shape = CircleShape,
            color = Theme,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Tambah Transaksi",
                    modifier = Modifier.size(26.dp),
                    tint = Color.White
                )
            }
        }
    }
}