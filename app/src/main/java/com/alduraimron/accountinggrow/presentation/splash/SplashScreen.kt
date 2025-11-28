package com.alduraimron.accountinggrow.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alduraimron.accountinggrow.R
import com.alduraimron.accountinggrow.ui.theme.AccountingGrowTheme
import com.alduraimron.accountinggrow.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2BA7FE),  // Blue top
                        Color(0xFFF5F8FF),  // Light blue bottom
                        Color(0xFFFFFFFF)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_accounting_grow),
                contentDescription = "Logo Accounting Grow",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Name
            Text(
                text = "Accounting",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2BA7FE),
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Grow",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2BA7FE),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AccountingGrowTheme {
        SplashScreen(onNavigateToHome = {})
    }
}