package com.example.myduitgw.navigation
import com.example.myduitgw.core.components.AppBottomBar
import android.app.Activity
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myduitgw.data.remote.auth.GoogleAuthManager
import com.example.myduitgw.ui.auth.WelcomeScreen
import com.example.myduitgw.ui.home.HomeScreen
import com.example.myduitgw.ui.auth.SplashScreen
import com.example.myduitgw.ui.auth.GoogleAuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import com.example.myduitgw.ui.home.HomeViewModel
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import com.example.myduitgw.core.theme.Theme
import com.example.myduitgw.ui.transaction.TransactionFormScreen
import com.example.myduitgw.ui.transaction.TransactionHistoryScreen
import com.example.myduitgw.ui.transaction.TransactionType
import com.example.myduitgw.ui.transfer.TransferScreen
import com.example.myduitgw.ui.transaction.TransactionHistoryViewModel
import com.example.myduitgw.ui.transaction.TransactionViewModel
import com.example.myduitgw.ui.transfer.TransferViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var splashFinished by remember { mutableStateOf(false) }

    val authViewModel: GoogleAuthViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                GoogleAuthViewModel(
                    context = context.applicationContext
                )
            }
        }
    )

    val homeViewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    context = context.applicationContext
                )
            }
        }
    )

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                TransactionViewModel(
                    context = context.applicationContext
                )
            }
        }
    )

    val transactionHistoryViewModel: TransactionHistoryViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                TransactionHistoryViewModel(
                    context = context.applicationContext
                )
            }
        }
    )

    val transferViewModel: TransferViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                TransferViewModel(
                    context = context.applicationContext
                )
            }
        }
    )

    val authUiState by authViewModel.uiState.collectAsState()
    val homeUiState by homeViewModel.uiState.collectAsState()
    val transactionUiState by transactionViewModel.uiState.collectAsState()
    val transactionHistoryUiState by transactionHistoryViewModel.uiState.collectAsState()
    val transferUiState by transferViewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in Routes.bottomBarRoutes

    val scope = rememberCoroutineScope()

    LaunchedEffect(authUiState.message) {
        val message = authUiState.message ?: return@LaunchedEffect

        Toast.makeText(
            context,
            message,
            if (authUiState.isError) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()

        authViewModel.clearMessage()
    }

    LaunchedEffect(homeUiState.isLoggedOut) {
        if (homeUiState.isLoggedOut) {

            authViewModel.onLogout()

            navController.navigate(Routes.WELCOME) {
                popUpTo(Routes.HOME) {
                    inclusive = true
                }
                launchSingleTop = true
            }

            homeViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(
        splashFinished,
        authUiState.isCheckingSession,
        authUiState.isAuthenticated,
        currentRoute
    ) {
        when {
            currentRoute == Routes.SPLASH &&
                    splashFinished &&
                    !authUiState.isCheckingSession -> {

                val destination = if (authUiState.isAuthenticated) {
                    Routes.HOME
                } else {
                    Routes.WELCOME
                }

                navController.navigate(destination) {
                    popUpTo(Routes.SPLASH) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            currentRoute == Routes.WELCOME &&
                    authUiState.isAuthenticated -> {

                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.WELCOME) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.checkSession()
    }

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
            AnimatedVisibility(
                visible = showBottomBar,
            ) {
                if (showBottomBar) {
                    AppBottomBar(
                        currentRoute = currentRoute,
                        onNavigateTab = { route ->

                            if (route == currentRoute) {

                                if (route == Routes.HOME) {
                                    homeViewModel.getHomeData()
                                }

                            } else {

                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        onIncomeClick = {
                            navController.navigate(Routes.INCOME)
                        },
                        onExpenseClick = {
                            navController.navigate(Routes.EXPENSE)
                        },
                        onTransferClick = {
                            navController.navigate(Routes.TRANSFER)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding),

            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it / 5 },
                    animationSpec = tween(250)
                ) + fadeIn(
                    animationSpec = tween(200)
                )
            },

            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 5 },
                    animationSpec = tween(250)
                ) + fadeOut(
                    animationSpec = tween(200)
                )
            },

            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 5 },
                    animationSpec = tween(250)
                ) + fadeIn(
                    animationSpec = tween(200)
                )
            },

            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it / 5 },
                    animationSpec = tween(250)
                ) + fadeOut(
                    animationSpec = tween(200)
                )
            }
        ) {

            composable(Routes.SPLASH) {
                SplashScreen(
                    playLogoAnimation = !authUiState.isAuthenticated,
                    onFinished = {
                        splashFinished = true
                    }
                )
            }

            composable(Routes.WELCOME) {
                WelcomeScreen(
                    isLoading = authUiState.isLoading,
                    onGoogleSignIn = {
                        scope.launch {
                            try {
                                val manager = googleAuthManager
                                    ?: throw IllegalStateException(
                                        "Activity tidak ditemukan."
                                    )

                                val idToken = manager.signIn()
                                authViewModel.loginWithGoogle(idToken)

                            } catch (exception: Exception) {
                                Log.e(
                                    "GoogleAuth",
                                    "Sign-in failed",
                                    exception
                                )

                                Toast.makeText(
                                    context,
                                    "${exception.javaClass.name}: ${exception.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = homeViewModel,
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
                    },
                    onLogout = {
                        homeViewModel.logout()
                    }
                )
            }

            composable(Routes.INCOME) {
                TransactionFormScreen(
                    type = TransactionType.INCOME,
                    viewModel = transactionViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.EXPENSE) {
                TransactionFormScreen(
                    type = TransactionType.EXPENSE,
                    viewModel = transactionViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.TRANSACTIONSHISTORY) {
                TransactionHistoryScreen(
                    viewModel = transactionHistoryViewModel,
                )
            }

            composable(Routes.TRANSFER) {
                TransferScreen(
                    viewModel = transferViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    if (homeUiState.isLoading || transactionUiState.isLoading || transactionHistoryUiState.isLoading || transferUiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {},
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Theme,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
//            composable(Routes.TRANSACTIONS) {
//                TransactionsScreen(store = store)
//            }
//            composable(Routes.SAVINGS) {
//                SavingsScreen(
//                    store = store,
//                    onAddSavings = { navController.navigate(Routes.ADD_SAVINGS) }
//                )
//            }
//            composable(Routes.PROFILE) {
//                ProfileScreen(
//                    store = store,
//                    onOpenStatistics = { navController.navigate(Routes.STATISTICS) }
//                )
//            }
//            composable(Routes.ADD_TRANSACTION) {
//                AddTransactionScreen(
//                    store = store,
//                    onBack = { navController.popBackStack() }
//                )
//            }
//            composable(Routes.ADD_SAVINGS) {
//                AddSavingsScreen(
//                    store = store,
//                    onBack = { navController.popBackStack() }
//                )
//            }
//            composable(Routes.STATISTICS) {
//                StatisticsScreen(
//                    store = store,
//                    onBack = { navController.popBackStack() }
//                )
//            }
//        }
//    }
//}

//@Composable
//fun AppBottomBar(
//    currentRoute: String?,
//    onNavigateTab: (String) -> Unit,
//    onAddTransaction: () -> Unit
//) {
//    Box(
//        modifier = Modifier.fillMaxWidth(),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(72.dp),
//            color = Color.White,
//            shadowElevation = 12.dp
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                BottomBarItem(
//                    icon = Icons.Rounded.Home,
//                    label = "Home",
//                    selected = currentRoute == Routes.HOME,
//                    onClick = { onNavigateTab(Routes.HOME) }
//                )
//                BottomBarItem(
//                    icon = Icons.AutoMirrored.Rounded.List,
//                    label = "Transaksi",
//                    selected = currentRoute == Routes.TRANSACTIONS,
//                    onClick = { onNavigateTab(Routes.TRANSACTIONS) }
//                )
//                Box(modifier = Modifier.size(68.dp))
//                BottomBarItem(
//                    icon = Icons.Rounded.Savings,
//                    label = "Tabungan",
//                    selected = currentRoute == Routes.SAVINGS,
//                    onClick = { onNavigateTab(Routes.SAVINGS) }
//                )
//                BottomBarItem(
//                    icon = Icons.Rounded.Person,
//                    label = "Profil",
//                    selected = currentRoute == Routes.PROFILE,
//                    onClick = { onNavigateTab(Routes.PROFILE) }
//                )
//            }
//        }
//
//        AddTransactionButton(onClick = onAddTransaction)
//    }
//}
//
//@Composable
//private fun BottomBarItem(
//    icon: ImageVector,
//    label: String,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .size(width = 68.dp, height = 64.dp)
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                modifier = Modifier.size(23.dp),
//                tint = if (selected) Theme else Color.Gray
//            )
//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelSmall,
//                color = if (selected) Theme else Color.Gray,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//    }
//}
//
//@Composable
//private fun AddTransactionButton(onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .size(68.dp)
//            .offset(y = (-24).dp)
//            .zIndex(2f),
//        contentAlignment = Alignment.Center
//    ) {
//        Surface(
//            onClick = onClick,
//            modifier = Modifier
//                .size(52.dp)
//                .shadow(elevation = 8.dp, shape = CircleShape),
//            shape = CircleShape,
//            color = Theme
//        ) {
//            Box(contentAlignment = Alignment.Center) {
//                Icon(
//                    imageVector = Icons.Rounded.Add,
//                    contentDescription = "Tambah Transaksi",
//                    modifier = Modifier.size(26.dp),
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}
