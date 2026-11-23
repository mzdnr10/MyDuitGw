package com.example.myduitgw.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.LocalParking
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myduitgw.ui.transaction.TransactionType
import java.time.LocalDate

class HomeResult {
}

data class DailyExpense(
    val date: String,
    val day: String,
    val expense: Long
)

data class Transaction(
    val id: String,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val category: String? = null,
    val date: LocalDate,
    val description: String = ""
)
enum class Category(
    val label: String,
    val type: TransactionType
) {
    // INCOME
    SALARY("Gaji Bulanan", TransactionType.INCOME),
    FREELANCE("Kerja Freelance", TransactionType.INCOME),
    INVESTMENT_RETURN("Investasi Return", TransactionType.INCOME),
    PERSONAL_GIFT_INCOME("Hadiah Personal", TransactionType.INCOME),
    OTHER_INCOME("Pendapatan Lain", TransactionType.INCOME),
    ANNUAL_BONUS("Bonus Tahunan", TransactionType.INCOME),
    WORK_ALLOWANCE("Tunjangan Kerja", TransactionType.INCOME),
    SALES_REVENUE("Hasil Penjualan", TransactionType.INCOME),
    SALES_COMMISSION("Komisi Penjualan", TransactionType.INCOME),
    SIDE_BUSINESS("Usaha Sampingan", TransactionType.INCOME),
    SAVINGS_INTEREST("Bunga Tabungan", TransactionType.INCOME),
    SHOPPING_CASHBACK("Cashback Belanja", TransactionType.INCOME),
    STOCK_DIVIDEND("Dividen Saham", TransactionType.INCOME),
    PROPERTY_RENT("Sewa Properti", TransactionType.INCOME),
    REFUND("Dana Refund", TransactionType.INCOME),

    // EXPENSE
    DAILY_MEALS("Makan Harian", TransactionType.EXPENSE),
    PUBLIC_TRANSPORT("Transportasi Umum", TransactionType.EXPENSE),
    GROCERIES("Belanja Kebutuhan", TransactionType.EXPENSE),
    MONTHLY_BILLS("Tagihan Bulanan", TransactionType.EXPENSE),
    MEDICAL("Kesehatan Medis", TransactionType.EXPENSE),
    DIGITAL_ENTERTAINMENT("Hiburan Digital", TransactionType.EXPENSE),
    EDUCATION("Biaya Pendidikan", TransactionType.EXPENSE),
    OTHER_EXPENSE("Pengeluaran Lain", TransactionType.EXPENSE),
    RESTAURANT("Makan Restoran", TransactionType.EXPENSE),
    DAILY_SNACKS("Jajan Harian", TransactionType.EXPENSE),
    COFFEE_DRINKS("Kopi dan Minuman", TransactionType.EXPENSE),
    FUEL("Bensin Kendaraan", TransactionType.EXPENSE),
    PARKING("Parkir Kendaraan", TransactionType.EXPENSE),
    VEHICLE_SERVICE("Servis Kendaraan", TransactionType.EXPENSE),
    ONLINE_TRANSPORT("Transportasi Online", TransactionType.EXPENSE),
    CLOTHING("Belanja Pakaian", TransactionType.EXPENSE),
    HOME_EQUIPMENT("Peralatan Rumah", TransactionType.EXPENSE),
    PERSONAL_CARE("Perawatan Pribadi", TransactionType.EXPENSE),
    ELECTRICITY("Listrik Rumah", TransactionType.EXPENSE),
    WATER_BILL("Air Bulanan", TransactionType.EXPENSE),
    INTERNET("Internet Rumah", TransactionType.EXPENSE),
    PHONE_CREDIT("Pulsa Telepon", TransactionType.EXPENSE),
    RENT("Biaya Kontrak", TransactionType.EXPENSE),
    MEDICINE("Obat dan Vitamin", TransactionType.EXPENSE),
    DENTAL_CARE("Perawatan Gigi", TransactionType.EXPENSE),
    DIGITAL_SUBSCRIPTIONS("Langganan Digital", TransactionType.EXPENSE),
    ENTERTAINMENT_TICKETS("Tiket Hiburan", TransactionType.EXPENSE),
    TRAVEL("Rekreasi Wisata", TransactionType.EXPENSE),
    BOOKS_COURSES("Buku dan Kursus", TransactionType.EXPENSE),
    DONATION("Donasi Sosial", TransactionType.EXPENSE),
    PERSONAL_GIFT_EXPENSE("Hadiah Personal", TransactionType.EXPENSE),
    ADMIN_FEES("Biaya Administrasi", TransactionType.EXPENSE),
    CREDIT_INSTALLMENT("Cicilan Kredit", TransactionType.EXPENSE);

    val icon: ImageVector
        get() = when (this) {
            SALARY, FREELANCE, ANNUAL_BONUS,
            WORK_ALLOWANCE, SIDE_BUSINESS,
            SALES_COMMISSION -> Icons.Rounded.Work

            INVESTMENT_RETURN, SAVINGS_INTEREST,
            STOCK_DIVIDEND -> Icons.Rounded.TrendingUp

            PERSONAL_GIFT_INCOME, PERSONAL_GIFT_EXPENSE,
            REFUND -> Icons.Rounded.CardGiftcard

            SALES_REVENUE -> Icons.Rounded.Store

            PROPERTY_RENT -> Icons.Rounded.Home

            SHOPPING_CASHBACK -> Icons.Rounded.ShoppingBag

            DAILY_MEALS, RESTAURANT,
            DAILY_SNACKS -> Icons.Rounded.Restaurant

            PUBLIC_TRANSPORT, ONLINE_TRANSPORT ->
                Icons.Rounded.DirectionsBus

            GROCERIES -> Icons.Rounded.LocalGroceryStore

            MONTHLY_BILLS, ELECTRICITY,
            WATER_BILL, INTERNET, PHONE_CREDIT ->
                Icons.Rounded.Receipt

            MEDICAL, MEDICINE -> Icons.Rounded.LocalHospital

            DIGITAL_ENTERTAINMENT, ENTERTAINMENT_TICKETS ->
                Icons.Rounded.Movie

            EDUCATION, BOOKS_COURSES ->
                Icons.Rounded.School

            FUEL -> Icons.Rounded.LocalGasStation

            PARKING -> Icons.Rounded.LocalParking

            VEHICLE_SERVICE -> Icons.Rounded.Build

            CLOTHING -> Icons.Rounded.Checkroom

            HOME_EQUIPMENT, RENT -> Icons.Rounded.Home

            PERSONAL_CARE, DENTAL_CARE ->
                Icons.Rounded.Face

            COFFEE_DRINKS -> Icons.Rounded.Coffee

            DIGITAL_SUBSCRIPTIONS -> Icons.Rounded.Subscriptions

            TRAVEL -> Icons.Rounded.Flight

            DONATION -> Icons.Rounded.VolunteerActivism

            ADMIN_FEES, CREDIT_INSTALLMENT ->
                Icons.Rounded.Payments

            OTHER_INCOME, OTHER_EXPENSE ->
                Icons.Rounded.MoreHoriz
        }

    companion object {
        fun fromLabel(label: String): Category? {
            return entries.find {
                it.label.equals(label, ignoreCase = true)
            }
        }
    }
}
