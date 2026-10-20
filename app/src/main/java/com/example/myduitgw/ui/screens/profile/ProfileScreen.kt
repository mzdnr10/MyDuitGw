package com.example.myduitgw.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.data.FinanceStore
import com.example.myduitgw.data.MockData
import com.example.myduitgw.ui.components.ColoredHeader
import com.example.myduitgw.ui.components.OverlappingHeaderScreen
import com.example.myduitgw.ui.components.ProfileAvatar
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.material.Radius
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.material.white
import com.example.myduitgw.util.toRupiah

@Composable
fun ProfileScreen(
    store: FinanceStore,
    onOpenStatistics: () -> Unit
) {
    val summary = store.summary()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var dialogTitle by remember { mutableStateOf<String?>(null) }

    OverlappingHeaderScreen(
        headerHeight = 200.dp,
        overlap = 56.dp,
        header = {
            ColoredHeader(height = 200.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar(size = 64.dp)
                    Spacer(modifier = Modifier.size(Spacing.Normal))
                    Column {
                        Text(
                            text = MockData.userName,
                            color = white,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(Spacing.Small))
                        Text(
                            text = "Anggota MyDuitGw",
                            color = white.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryMini(label = "Saldo", value = summary.totalBalance.toRupiah())
            SummaryMini(label = "Masuk", value = summary.monthlyIncome.toRupiah())
            SummaryMini(label = "Keluar", value = summary.monthlyExpense.toRupiah())
        }
        Spacer(modifier = Modifier.height(Spacing.Large))
        Text(
            text = "Pengaturan",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(Spacing.Normal))
        ProfileOption(
            icon = Icons.Rounded.Edit,
            title = "Edit Profil",
            onClick = { dialogTitle = "Edit Profil" }
        )
        ProfileOption(
            icon = Icons.Rounded.Notifications,
            title = "Notifikasi",
            trailing = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = Theme)
                )
            }
        )
        ProfileOption(
            icon = Icons.Rounded.DarkMode,
            title = "Tampilan",
            onClick = { dialogTitle = "Tampilan" }
        )
        ProfileOption(
            icon = Icons.Rounded.Category,
            title = "Kategori",
            onClick = { dialogTitle = "Kategori" }
        )
        ProfileOption(
            icon = Icons.Rounded.BarChart,
            title = "Statistik Keuangan",
            onClick = onOpenStatistics
        )
        ProfileOption(
            icon = Icons.Rounded.Info,
            title = "Tentang Aplikasi",
            onClick = { dialogTitle = "Tentang Aplikasi" }
        )
        Spacer(modifier = Modifier.height(Spacing.Large))
    }

    dialogTitle?.let { title ->
        AlertDialog(
            onDismissRequest = { dialogTitle = null },
            confirmButton = {
                TextButton(onClick = { dialogTitle = null }) {
                    Text("Tutup")
                }
            },
            title = { Text(title) },
            text = {
                Text(
                    when (title) {
                        "Edit Profil" -> "Pengeditan profil akan tersedia setelah akun dihubungkan."
                        "Tampilan" -> "Aplikasi saat ini menggunakan tema terang dengan warna utama biru."
                        "Kategori" -> "Kategori pemasukan dan pengeluaran sudah tersedia di form transaksi."
                        else -> "MyDuitGw adalah aplikasi manajemen keuangan pribadi. Bukan e-wallet atau mobile banking."
                    }
                )
            }
        )
    }
}

@Composable
private fun SummaryMini(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(Grey50, RoundedCornerShape(Radius.Medium))
            .padding(horizontal = Spacing.Medium, vertical = Spacing.Normal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
private fun ProfileOption(
    icon: ImageVector,
    title: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = Theme)
        Spacer(modifier = Modifier.size(Spacing.Normal))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color.DarkGray
        )
        if (trailing != null) {
            trailing()
        } else {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}
