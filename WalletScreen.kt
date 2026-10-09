package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.TransactionItem
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    currentUser: User,
    onClaimDemoCoins: (Long) -> Unit
) {
    var transactions by remember { mutableStateOf(MockDataProvider.sampleTransactions) }
    var showBonusSnackBar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Wallet & Demo Coins",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Sandbox Test Mode",
                    fontSize = 11.sp,
                    color = CoinGold,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Balance Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF3B1D82),
                                    Color(0xFF831843),
                                    Color(0xFF1E1B4B)
                                )
                            )
                        )
                        .border(1.5.dp, NeonPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AVAILABLE DEMO BALANCE",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 1.sp
                            )
                            Text(text = "🪙 TEST COINS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoinGold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${currentUser.demoCoins}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Diamonds Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "💎 Diamonds: ${currentUser.diamonds}", color = DiamondCyan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "• Level ${currentUser.level} VIP", color = CoinGold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Free Test Coins Claim Buttons
            item {
                Text(
                    text = "Claim Free Test Coins (Zero Real Money)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        5000L to "+5k",
                        10000L to "+10k",
                        25000L to "+25k",
                        50000L to "+50k"
                    ).forEach { (amount, label) ->
                        Button(
                            onClick = {
                                onClaimDemoCoins(amount)
                                transactions = listOf(
                                    TransactionItem(
                                        id = "tx_${System.currentTimeMillis()}",
                                        title = "Free Test Grant: $label",
                                        subtitle = "Prototype demo replenishment",
                                        amount = amount,
                                        isCredit = true,
                                        timestamp = "Just now"
                                    )
                                ) + transactions
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("claim_btn_$label")
                        ) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                color = CoinGold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Transaction History Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions (${transactions.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Ledger Verified ✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen
                    )
                }
            }

            // Transaction Items
            items(transactions) { tx ->
                TransactionRowItem(transaction = tx)
            }
        }
    }
}

@Composable
fun TransactionRowItem(transaction: TransactionItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (transaction.isCredit) SuccessGreen.copy(alpha = 0.15f) else NeonPink.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (transaction.isCredit) "⬇️" else "⬆️",
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${transaction.subtitle} • ${transaction.timestamp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            Text(
                text = "${if (transaction.isCredit) "+" else "-"}${transaction.amount}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.isCredit) SuccessGreen else NeonPink
            )
        }
    }
}
