package com.alduraimron.accountinggrow.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alduraimron.accountinggrow.R
import com.alduraimron.accountinggrow.ui.theme.AccountingGrowTheme
import com.alduraimron.accountinggrow.ui.theme.Gray500
import com.alduraimron.accountinggrow.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.onboarding_1,
            title = "SELAMAT DATANG",
            description = "Mulai perjalanan mu bareng\naccounting grow"
        ),
        OnboardingPage(
            image = R.drawable.onboarding_2,
            title = "CATAT KEUANGAN ANDA",
            description = "Mencatat transaksi lebih mudah\ncepat dan praktis"
        ),
        OnboardingPage(
            image = R.drawable.onboarding_3,
            title = "INVOICE CEPAT LUNAS,\nTRANSAKSI MAKIN MUDAH",
            description = "Buat Invoice dan monitor seluruh status\ntransaksi dalam satu platform dengan\nberbagai fitur menarik."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageContent(pages[page])
            }

            // Page Indicators
            Row(
                modifier = Modifier.padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(
                                width = if (isSelected) 24.dp else 8.dp,
                                height = 8.dp
                            )
                            .clip(CircleShape)
                            .background(
                                if (isSelected) PrimaryBlue else Gray500.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            // Main Button - MASUK (untuk yang sudah punya akun)
            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Masuk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Bottom Register Button - DAFTAR (untuk yang belum punya akun)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum Punya Akun ? ",
                    color = Gray500,
                    fontSize = 13.sp
                )
                TextButton(
                    onClick = onRegister,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Daftar",
                        color = PrimaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Illustration
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 32.dp)
        )

        // Title
        Text(
            text = page.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = page.description,
            fontSize = 14.sp,
            color = Gray500,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    AccountingGrowTheme {
        OnboardingScreen(
            onLogin = {},
            onRegister = {}
        )
    }
}