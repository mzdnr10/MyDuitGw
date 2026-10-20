package com.example.myduitgw.navigation

object Routes {
    const val HOME = "home"
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val TRANSACTIONS = "transactions"
    const val SAVINGS = "savings"
    const val PROFILE = "profile"
    const val ADD_TRANSACTION = "add_transaction"
    const val ADD_SAVINGS = "add_savings"
    const val STATISTICS = "statistics"

    val bottomBarRoutes = setOf(HOME, TRANSACTIONS, SAVINGS, PROFILE)
}
