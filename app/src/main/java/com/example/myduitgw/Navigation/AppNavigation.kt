package com.example.myduitgw.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.clickable
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myduitgw.data.FinanceStore
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.screens.addtransaction.AddTransactionScreen
import com.example.myduitgw.ui.screens.home.HomeScreen
import com.example.myduitgw.ui.screens.profile.ProfileScreen
import com.example.myduitgw.ui.screens.savings.AddSavingsScreen
import com.example.myduitgw.ui.screens.savings.SavingsScreen
import com.example.myduitgw.ui.screens.statistics.StatisticsScreen
import com.example.myduitgw.ui.screens.transactions.TransactionsScreen

import com.example.myduitgw.ui.screens.splash.SplashScreen
import com.example.myduitgw.ui.screens.welcome.WelcomeScreen
import com.example.myduitgw.AuthApi

//animation navigation
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.myduitgw.auth.GoogleAuthManager
import android.widget.Toast


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val store = remember { FinanceStore() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in Routes.bottomBarRoutes

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val activity = remember(context) {
        var current = context
        while (current !is Activity && current is ContextWrapper) {
            current = current.baseContext
        }
        current as? Activity
    }

    val googleAuthManager = remember(activity) {
        activity?.let { GoogleAuthManager(it) }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigateTab = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onAddTransaction = {
                        navController.navigate(Routes.ADD_TRANSACTION)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding),

            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Routes.WELCOME) {
                            popUpTo(Routes.SPLASH) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Routes.WELCOME) {
                WelcomeScreen(
                    onGoogleSignIn = {
                        scope.launch {
                            try {
                                val manager = googleAuthManager
                                    ?: throw IllegalStateException(
                                        "Activity tidak ditemukan."
                                    )

                                val idToken = manager.signIn()

                                val result = AuthApi.loginWithGoogle(idToken)

                                android.util.Log.d(
                                    "GoogleAuth",
                                    "Backend login berhasil untuk ${result.email}"
                                )

                                Toast.makeText(
                                    context,
                                    "Login backend berhasil!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } catch (e: Exception) {
                                android.util.Log.e("GoogleAuth", "Sign-in failed", e)

                                Toast.makeText(
                                    context,
                                    "${e.javaClass.name}: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }
            // Composable Home dan halaman lainnya tetap di bawah sini.
            composable(Routes.HOME) {
                HomeScreen(
                    store = store,
                    onNavigate = { route ->
                        if (route in Routes.bottomBarRoutes) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            navController.navigate(route)
                        }
                    }
                )
            }
            composable(Routes.TRANSACTIONS) {
                TransactionsScreen(store = store)
            }
            composable(Routes.SAVINGS) {
                SavingsScreen(
                    store = store,
                    onAddSavings = { navController.navigate(Routes.ADD_SAVINGS) }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    store = store,
                    onOpenStatistics = { navController.navigate(Routes.STATISTICS) }
                )
            }
            composable(Routes.ADD_TRANSACTION) {
                AddTransactionScreen(
                    store = store,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.ADD_SAVINGS) {
                AddSavingsScreen(
                    store = store,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.STATISTICS) {
                StatisticsScreen(
                    store = store,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

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
            shadowElevation = 12.dp
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
                Box(modifier = Modifier.size(68.dp))
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
            color = Theme
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
