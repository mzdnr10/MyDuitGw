package com.example.myduitgw.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.R
import kotlinx.coroutines.delay
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import com.example.myduitgw.ui.theme.Grey600
import com.example.myduitgw.ui.theme.Theme
import com.example.myduitgw.ui.theme.white

@Composable
fun SplashScreen(
    playLogoAnimation: Boolean,
    onFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = tween(durationMillis = 650),
        label = "logoScale"
    )
//
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 650),
        label = "logoAlpha"
    )

    var movingUp by remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (movingUp) (-135).dp else 0.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "logoMoveUp"
    )

    LaunchedEffect(Unit) {
        visible = true
        // Logo diam di tengah terlebih dahulu
        delay(1700)
        // Mulai bergerak naik
        movingUp = true
        // Tunggu animasi selesai sebelum pindah screen
        delay(100)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.myduitgw_logo),
                contentDescription = "Logo MyDuitGw",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale),
//                    .offset(y = offsetY),
                alpha = alpha
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MyDuitGw",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Theme,
                textAlign = TextAlign.Center,
//                modifier = Modifier.offset(y = offsetY)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Manage your money, your way.",
                fontSize = 15.sp,
                color = Grey600,
                textAlign = TextAlign.Center,
//                modifier = Modifier.offset(y = offsetY)
            )
        }
    }
}
