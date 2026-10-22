package com.example.myduitgw.ui.screens.savings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.data.repository.FinanceStore
import com.example.myduitgw.ui.components.ColoredHeader
import com.example.myduitgw.ui.components.OverlappingHeaderScreen
import com.example.myduitgw.ui.components.SavingsGoalCard
import com.example.myduitgw.ui.material.Grey50
import com.example.myduitgw.ui.material.Radius
import com.example.myduitgw.ui.material.Spacing
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.material.white
import com.example.myduitgw.util.toRupiah

@Composable
fun SavingsScreen(
    store: FinanceStore,
    onAddSavings: () -> Unit
) {
    val totalSaved = store.savingsGoals.sumOf { it.currentAmount }

    OverlappingHeaderScreen(
        headerHeight = 180.dp,
        overlap = 48.dp,
        header = {
            ColoredHeader(height = 180.dp) {
                Text(
                    text = "Tabungan",
                    color = white,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Spacing.Small))
                Text(
                    text = "Total terkumpul",
                    color = white.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(Spacing.Small))
                Text(
                    text = totalSaved.toRupiah(),
                    color = white,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        Text(
            text = "Target Tabungan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(Spacing.Normal))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.Normal)) {
            store.savingsGoals.forEach { goal ->
                SavingsGoalCard(goal = goal)
            }
        }
        Spacer(modifier = Modifier.height(Spacing.Large))
        Button(
            onClick = onAddSavings,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(Radius.Medium),
            colors = ButtonDefaults.buttonColors(containerColor = Theme)
        ) {
            Text("Tambah Tabungan")
        }
        Spacer(modifier = Modifier.height(Spacing.Large))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingsScreen(
    store: FinanceStore,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }
    var currentText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Grey50,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Tambah Tabungan", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme,
                    titleContentColor = white,
                    navigationIconContentColor = white
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.Normal)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama tabungan") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Spacing.Normal))
            OutlinedTextField(
                value = targetText,
                onValueChange = { value -> targetText = value.filter { it.isDigit() } },
                label = { Text("Target") },
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Spacing.Normal))
            OutlinedTextField(
                value = currentText,
                onValueChange = { value -> currentText = value.filter { it.isDigit() } },
                label = { Text("Jumlah saat ini") },
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
            Button(
                onClick = {
                    val target = targetText.toLongOrNull() ?: 0L
                    val current = currentText.toLongOrNull() ?: 0L
                    if (name.isNotBlank() && target > 0L) {
                        store.addSavingsGoal(
                            name = name,
                            targetAmount = target,
                            currentAmount = current
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(Radius.Medium),
                colors = ButtonDefaults.buttonColors(containerColor = Theme),
                enabled = name.isNotBlank() && (targetText.toLongOrNull() ?: 0L) > 0L
            ) {
                Text("Simpan")
            }
        }
    }
}
