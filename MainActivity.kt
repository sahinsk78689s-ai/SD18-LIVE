package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.ui.components.FreeDemoCoinsDialog
import com.example.ui.components.TopBarHeader
import com.example.ui.screens.*
import com.example.ui.theme.*

enum class AppDestination {
    HOME,
    VOICE_ROOMS,
    CHAT,
    WALLET,
    PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentUser by remember { mutableStateOf(MockDataProvider.currentUser) }
    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
    var rooms by remember { mutableStateOf(MockDataProvider.liveRooms) }

    // Active full-screen interactive states
    var activeVoiceRoom by remember { mutableStateOf<LiveRoom?>(null) }
    var activeLiveVideoRoom by remember { mutableStateOf<LiveRoom?>(null) }
    var activeCallPeer by remember { mutableStateOf<Pair<User, Boolean>?>(null) } // (peer, isVideo)
    var showAgencyScreen by remember { mutableStateOf(false) }
    var showCoinsDialog by remember { mutableStateOf(false) }

    // Handle back button for sub-screens
    BackHandler(
        enabled = activeVoiceRoom != null || activeLiveVideoRoom != null || activeCallPeer != null || showAgencyScreen
    ) {
        when {
            activeCallPeer != null -> activeCallPeer = null
            activeVoiceRoom != null -> activeVoiceRoom = null
            activeLiveVideoRoom != null -> activeLiveVideoRoom = null
            showAgencyScreen -> showAgencyScreen = false
        }
    }

    // 1. Audio / Video Call Screen
    if (activeCallPeer != null) {
        val (peer, isVideo) = activeCallPeer!!
        CallScreen(
            peerUser = peer,
            isVideo = isVideo,
            onEndCall = { activeCallPeer = null }
        )
        return
    }

    // 2. 12-Seat Voice Party Room Screen
    if (activeVoiceRoom != null) {
        VoiceRoomScreen(
            room = activeVoiceRoom!!,
            currentUser = currentUser,
            onClose = { activeVoiceRoom = null },
            onSendGift = { gift, combo, targetUser ->
                val cost = gift.coinPrice * combo
                if (currentUser.demoCoins >= cost) {
                    currentUser = currentUser.copy(demoCoins = currentUser.demoCoins - cost)
                }
            },
            onAddCoins = { showCoinsDialog = true }
        )

        if (showCoinsDialog) {
            FreeDemoCoinsDialog(
                onDismiss = { showCoinsDialog = false },
                onClaim = { amount ->
                    currentUser = currentUser.copy(demoCoins = currentUser.demoCoins + amount)
                    showCoinsDialog = false
                }
            )
        }
        return
    }

    // 3. Live Video Stream Screen
    if (activeLiveVideoRoom != null) {
        LiveVideoScreen(
            room = activeLiveVideoRoom!!,
            currentUser = currentUser,
            onClose = { activeLiveVideoRoom = null },
            onSendGift = { gift, combo, targetUser ->
                val cost = gift.coinPrice * combo
                if (currentUser.demoCoins >= cost) {
                    currentUser = currentUser.copy(demoCoins = currentUser.demoCoins - cost)
                }
            },
            onAddCoins = { showCoinsDialog = true }
        )

        if (showCoinsDialog) {
            FreeDemoCoinsDialog(
                onDismiss = { showCoinsDialog = false },
                onClaim = { amount ->
                    currentUser = currentUser.copy(demoCoins = currentUser.demoCoins + amount)
                    showCoinsDialog = false
                }
            )
        }
        return
    }

    // 4. Agency Hub Screen
    if (showAgencyScreen) {
        AgencyScreen(
            onBack = { showAgencyScreen = false }
        )
        return
    }

    // Main Scaffold with Bottom Navigation
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBg,
        topBar = {
            TopBarHeader(
                demoCoins = currentUser.demoCoins,
                onCoinClick = { showCoinsDialog = true },
                onAdminClick = { currentDestination = AppDestination.PROFILE },
                currentRole = currentUser.role
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.HOME,
                    onClick = { currentDestination = AppDestination.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonMagenta,
                        selectedTextColor = NeonMagenta,
                        indicatorColor = NeonPurple.copy(alpha = 0.25f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.VOICE_ROOMS,
                    onClick = { currentDestination = AppDestination.VOICE_ROOMS },
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Voice 12") },
                    label = { Text("Voice 12") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonPurpleLight,
                        selectedTextColor = NeonPurpleLight,
                        indicatorColor = NeonPurple.copy(alpha = 0.25f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_voice")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.CHAT,
                    onClick = { currentDestination = AppDestination.CHAT },
                    icon = { Icon(Icons.Default.ChatBubble, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonPurple.copy(alpha = 0.25f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_chat")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.WALLET,
                    onClick = { currentDestination = AppDestination.WALLET },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
                    label = { Text("Wallet") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoinGold,
                        selectedTextColor = CoinGold,
                        indicatorColor = NeonPurple.copy(alpha = 0.25f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_wallet")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.PROFILE,
                    onClick = { currentDestination = AppDestination.PROFILE },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonPurpleLight,
                        selectedTextColor = NeonPurpleLight,
                        indicatorColor = NeonPurple.copy(alpha = 0.25f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                AppDestination.HOME -> {
                    HomeScreen(
                        rooms = rooms,
                        onSelectRoom = { room ->
                            if (room.type == RoomType.VOICE_PARTY_12) {
                                activeVoiceRoom = room
                            } else {
                                activeLiveVideoRoom = room
                            }
                        },
                        onCreateRoom = {
                            val newPartyRoom = LiveRoom(
                                id = "room_${System.currentTimeMillis()}",
                                title = "${currentUser.name}'s 12-Seat Party 🎙️🔥",
                                host = currentUser,
                                type = RoomType.VOICE_PARTY_12,
                                viewerCount = 1,
                                category = "Voice Party",
                                tags = listOf("12 Seats", "Exclusive", "Party"),
                                seats = MockDataProvider.create12Seats(1)
                            )
                            rooms = listOf(newPartyRoom) + rooms
                            activeVoiceRoom = newPartyRoom
                        },
                        onNavigateToAgency = { showAgencyScreen = true }
                    )
                }

                AppDestination.VOICE_ROOMS -> {
                    // Dedicated 12-seats voice rooms hub
                    HomeScreen(
                        rooms = rooms.filter { it.type == RoomType.VOICE_PARTY_12 },
                        onSelectRoom = { activeVoiceRoom = it },
                        onCreateRoom = {
                            val newPartyRoom = LiveRoom(
                                id = "room_${System.currentTimeMillis()}",
                                title = "${currentUser.name}'s 12-Seat Lounge 🎙️",
                                host = currentUser,
                                type = RoomType.VOICE_PARTY_12,
                                viewerCount = 1,
                                category = "Voice Party",
                                tags = listOf("12 Seats", "Live Mics"),
                                seats = MockDataProvider.create12Seats(0)
                            )
                            rooms = listOf(newPartyRoom) + rooms
                            activeVoiceRoom = newPartyRoom
                        },
                        onNavigateToAgency = { showAgencyScreen = true }
                    )
                }

                AppDestination.CHAT -> {
                    ChatScreen(
                        currentUser = currentUser,
                        onStartCall = { peer, isVideo ->
                            activeCallPeer = Pair(peer, isVideo)
                        },
                        onAddCoins = { showCoinsDialog = true }
                    )
                }

                AppDestination.WALLET -> {
                    WalletScreen(
                        currentUser = currentUser,
                        onClaimDemoCoins = { amount ->
                            currentUser = currentUser.copy(demoCoins = currentUser.demoCoins + amount)
                        }
                    )
                }

                AppDestination.PROFILE -> {
                    ProfileAndAdminScreen(
                        currentUser = currentUser,
                        onRoleChanged = { newRole ->
                            currentUser = currentUser.copy(role = newRole)
                        },
                        onNavigateToAgency = { showAgencyScreen = true },
                        onUserLogin = { name, handle ->
                            currentUser = currentUser.copy(name = name, handle = handle)
                        },
                        onClaimCoins = { amount ->
                            currentUser = currentUser.copy(demoCoins = currentUser.demoCoins + amount)
                        }
                    )
                }
            }
        }
    }

    // Free Coins Dialog
    if (showCoinsDialog) {
        FreeDemoCoinsDialog(
            onDismiss = { showCoinsDialog = false },
            onClaim = { amount ->
                currentUser = currentUser.copy(demoCoins = currentUser.demoCoins + amount)
                showCoinsDialog = false
            }
        )
    }
}
