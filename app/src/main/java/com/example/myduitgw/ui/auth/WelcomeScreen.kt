
package com.example.myduitgw.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myduitgw.R
import com.example.myduitgw.ui.material.Blue50
import com.example.myduitgw.ui.material.Grey300
import com.example.myduitgw.ui.material.Grey600
import com.example.myduitgw.ui.material.Grey900
import com.example.myduitgw.ui.material.Theme
import com.example.myduitgw.ui.material.white

@Composable
fun WelcomeScreen(
    isLoading: Boolean,
    onGoogleSignIn: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Blue50
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ruang fleksibel di atas
            Spacer(modifier = Modifier.weight(1f))

            // Ilustrasi utama
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.myduitgw_logo),
                    contentDescription = "Logo MyDuitGw",
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Money,\nYour Control.",
                fontSize = 32.sp,
                lineHeight = 39.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Kelola keuanganmu dengan lebih mudah. " +
                        "Catat transaksi, pantau pengeluaran, " +
                        "dan capai tujuan finansialmu.",
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = Grey600,
                textAlign = TextAlign.Center
            )

            // Ruang fleksibel antara konten dan tombol
            Spacer(modifier = Modifier.weight(1f))

            // Tombol Google
            Button(
                onClick = onGoogleSignIn,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = white,
                    contentColor = Grey900
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Grey300
                )
            ) {
                Text(
                    text = "G",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Theme
                )

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "Continue with Google",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Dengan melanjutkan, kamu menyetujui " +
                        "Terms of Service dan Privacy Policy.",
                fontSize = 11.sp,
                lineHeight = 17.sp,
                color = Grey600,
                textAlign = TextAlign.Center
            )

            // Ruang fleksibel di bawah
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
