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
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun ProfileAndAdminScreen(
    currentUser: User,
    onRoleChanged: (UserRole) -> Unit,
    onNavigateToAgency: () -> Unit,
    onUserLogin: (String, String) -> Unit,
    onClaimCoins: (Long) -> Unit
) {
    var showAuthDialog by remember { mutableStateOf(false) }
    var showAdminBroadcastDialog by remember { mutableStateOf(false) }
    var broadcastMessage by remember { mutableStateOf<String?>(null) }
    var moderatedUsers by remember { mutableStateOf(MockDataProvider.users.toMutableList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Profile & System Roles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            TextButton(
                onClick = { showAuthDialog = true },
                modifier = Modifier.testTag("auth_dialog_btn")
            ) {
                Text("Switch / Login", color = NeonCyan)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF2E1065),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .border(1.5.dp, DarkBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant)
                                    .border(2.dp, NeonPurple, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = currentUser.avatarEmoji, fontSize = 32.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentUser.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(CoinGold)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "VIP ${currentUser.level}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Text(
                                    text = currentUser.handle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Role Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when (currentUser.role) {
                                                UserRole.SUPER_ADMIN -> LiveRed
                                                UserRole.AGENCY_BOSS -> NeonPurple
                                                UserRole.HOST -> NeonMagenta
                                                UserRole.USER -> NeonCyan
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Role: ${currentUser.role.name}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Role Switcher for Prototype Testing
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "🎭 Quick Role Switcher (Prototype Testing)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurpleLight
                        )
                        Text(
                            text = "Switch roles immediately to test Host, Agency Boss, or Super Admin permissions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            UserRole.values().forEach { role ->
                                val isCurrent = currentUser.role == role
                                Button(
                                    onClick = { onRoleChanged(role) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isCurrent) NeonPurple else DarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp)
                                        .testTag("role_btn_${role.name}")
                                ) {
                                    Text(
                                        text = role.name.substringBefore("_"),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Agency Navigation shortcut
            item {
                Card(
                    onClick = onNavigateToAgency,
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.testTag("agency_portal_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏢", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Agency & Host Management", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Recruitment, rosters & commission splits", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }

                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                    }
                }
            }

            // SUPER ADMIN SECTION (Active if Super Admin)
            if (currentUser.role == UserRole.SUPER_ADMIN) {
                item {
                    Text(
                        text = "🛡️ Super Admin Control Center",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = LiveRed
                    )
                }

                // Broadcast & System Action Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showAdminBroadcastDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = LiveRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_broadcast_btn")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("System Broadcast", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onClaimCoins(100000L) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CoinGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Grant 100k Coins", fontSize = 11.sp, color = CoinGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Broadcast confirmation banner if active
                broadcastMessage?.let { msg ->
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(LiveRed.copy(alpha = 0.2f))
                                .border(1.dp, LiveRed, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📢 ", fontSize = 16.sp)
                                Text(
                                    text = "Broadcast Active: \"$msg\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // User Moderation List
                item {
                    Text(
                        text = "User Moderation & Enforcement (${moderatedUsers.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                items(moderatedUsers) { u ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = u.avatarEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = u.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(text = "${u.handle} • Role: ${u.role.name}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = {
                                        moderatedUsers = moderatedUsers.map {
                                            if (it.id == u.id) it.copy(isBanned = !it.isBanned) else it
                                        }.toMutableList()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (u.isBanned) SuccessGreen else LiveRed
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp).testTag("ban_user_${u.id}")
                                ) {
                                    Text(
                                        text = if (u.isBanned) "Unban" else "Ban",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Auth / Signup & Login Dialog
        if (showAuthDialog) {
            AuthSignupLoginDialog(
                onDismiss = { showAuthDialog = false },
                onLogin = { name, handle ->
                    onUserLogin(name, handle)
                    showAuthDialog = false
                }
            )
        }

        // Admin Broadcast Dialog
        if (showAdminBroadcastDialog) {
            AlertDialog(
                onDismissRequest = { showAdminBroadcastDialog = false },
                containerColor = DarkSurface,
                title = { Text("Send System-Wide Broadcast", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    var input by remember { mutableStateOf("") }
                    Column {
                        Text("This banner displays across all active 12-seat rooms & video streams.", color = TextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = input,
                            onValueChange = { input = it },
                            placeholder = { Text("E.g., SD18 Star Carnival Starts in 10 mins!") },
                            modifier = Modifier.fillMaxWidth().testTag("broadcast_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            broadcastMessage = "SD18 Live Carnival is currently in progress! Enjoy 12-mic party rooms!"
                            showAdminBroadcastDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                    ) {
                        Text("Broadcast Now", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAdminBroadcastDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun AuthSignupLoginDialog(
    onDismiss: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var emailOrHandle by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = if (isSignUp) "Create SD18 LIVE Account" else "Welcome Back to SD18 LIVE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isSignUp) "Sign up with username and handle." else "Log in to your SD18 LIVE account.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                if (isSignUp) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        modifier = Modifier.fillMaxWidth().testTag("auth_name_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = emailOrHandle,
                    onValueChange = { emailOrHandle = it },
                    label = { Text("Handle / Email (e.g. @sujan_live)") },
                    modifier = Modifier.fillMaxWidth().testTag("auth_handle_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                TextButton(
                    onClick = { isSignUp = !isSignUp },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (isSignUp) "Already have account? Log In" else "New to SD18? Sign Up",
                        color = NeonPurpleLight,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val displayName = if (name.isNotBlank()) name else "SD18 Live Star"
                    val handle = if (emailOrHandle.isNotBlank()) emailOrHandle else "@user_sd18"
                    onLogin(displayName, handle)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("auth_confirm_btn")
            ) {
                Text(if (isSignUp) "Create Account" else "Log In", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
