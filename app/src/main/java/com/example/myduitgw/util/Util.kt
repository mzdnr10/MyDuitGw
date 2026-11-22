package com.example.myduitgw.util

import com.example.myduitgw.data.model.TransactionType
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val indonesianLocale = Locale.forLanguageTag("id-ID")
private val currencyFormat = NumberFormat.getInstance(indonesianLocale)
private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", indonesianLocale)
private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", indonesianLocale)

fun Long.toRupiah(): String = "Rp ${currencyFormat.format(this)}"

fun Long.toCompactRupiah(): String {
    return when {
        this >= 1_000_000_000L -> "Rp ${this / 1_000_000_000}M"
        this >= 1_000_000L -> {
            val value = this / 1_000_000.0
            if (value % 1.0 == 0.0) {
                "Rp ${value.toInt()}jt"
            } else {
                "Rp ${"%.1f".format(indonesianLocale, value)}jt"
            }
        }
        else -> toRupiah()
    }
}

fun Long.toSignedRupiah(type: TransactionType): String {
    val formatted = toRupiah()
    return if (type == TransactionType.INCOME) {
        "+ $formatted"
    } else {
        "- $formatted"
    }
}

fun LocalDate.toDisplayDate(): String = format(dateFormatter)

