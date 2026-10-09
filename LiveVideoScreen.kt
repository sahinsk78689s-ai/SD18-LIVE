package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.ui.components.FloatingGiftAnnouncement
import com.example.ui.components.GiftBottomSheet
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LiveVideoScreen(
    room: LiveRoom,
    currentUser: User,
    onClose: () -> Unit,
    onSendGift: (GiftItem, Int, User) -> Unit,
    onAddCoins: () -> Unit
) {
    var isFollowing by remember { mutableStateOf(false) }
    var showGiftSheet by remember { mutableStateOf(false) }
    var likesCount by remember { mutableStateOf(14200) }
    var floatingHearts by remember { mutableStateOf(listOf<Int>()) }
    var pkLeftScore by remember { mutableStateOf(room.pkScoreLeft) }
    var pkRightScore by remember { mutableStateOf(room.pkScoreRight) }
    var latestGiftAnnouncement by remember { mutableStateOf<Pair<GiftItem, Int>?>(null) }
    var commentText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("c1", MockDataProvider.users[0], "Welcome to the live stream! Drop some hearts! 💖", "Now", true),
                ChatMessage("c2", MockDataProvider.users[3], "Looking stunning tonight! 🔥", "Now"),
                ChatMessage("c3", MockDataProvider.users[5], "Let's win this PK battle team!! 🥊", "Now")
            )
        )
    }

    // Effect to hide gift announcement
    LaunchedEffect(latestGiftAnnouncement) {
        if (latestGiftAnnouncement != null) {
            delay(4000)
            latestGiftAnnouncement = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Simulated Live Stream Camera Feed (Cyberpunk Live Gradient)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF180D38),
                            Color(0xFF0F172A),
                            Color(0xFF0A0F1D)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Live Streamer Simulated Stage
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = room.host.avatarEmoji,
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${room.host.name} is LIVE",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "HD 1080p 60FPS • Low Latency Streaming",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonCyan
                )
            }
        }

        // Tap overlay to send hearts
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    likesCount++
                    floatingHearts = floatingHearts + (1..1000).random()
                }
        )

        // Floating Hearts Animation Effect
        floatingHearts.takeLast(6).forEach { heartId ->
            key(heartId) {
                FloatingHeartItem()
            }
        }

        // TOP HUD: Host Info + Viewers + Exit
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Host pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = room.host.avatarEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = room.host.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(LiveRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${room.viewerCount} Viewers",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Follow Button
                        Button(
                            onClick = { isFollowing = !isFollowing },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing) DarkBorder else NeonMagenta
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = if (isFollowing) "Following" else "+ Follow",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Exit Stream
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .testTag("exit_live_stream_btn")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Exit", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            // PK Battle Bar (if PK mode or enabled)
            if (room.type == RoomType.PK_BATTLE) {
                Spacer(modifier = Modifier.height(10.dp))
                PKBattleHeader(
                    leftHost = room.host,
                    rightHost = room.opponentHost ?: MockDataProvider.users[4],
                    leftScore = pkLeftScore,
                    rightScore = pkRightScore
                )
            }
        }

        // Floating Gift Announcement
        latestGiftAnnouncement?.let { (gift, combo) ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 80.dp)
            ) {
                FloatingGiftAnnouncement(
                    senderName = currentUser.name,
                    receiverName = room.host.name,
                    gift = gift,
                    combo = combo,
                    visible = true
                )
            }
        }

        // BOTTOM HUD: Live Comments & Actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Live Chat Feed
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.55f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${msg.sender.avatarEmoji} ${msg.sender.name}: ",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = NeonPurpleLight
                            )
                            Text(
                                text = msg.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row: Comment field + Heart like + Gift button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Add comment...", color = TextTertiary, fontSize = 12.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.5f),
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    trailingIcon = {
                        if (commentText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    messages = messages + ChatMessage(
                                        id = "cm_${System.currentTimeMillis()}",
                                        sender = currentUser,
                                        message = commentText.trim(),
                                        timestamp = "Now"
                                    )
                                    commentText = ""
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = NeonPurple)
                            }
                        }
                    }
                )

                // Hearts / Like button
                IconButton(
                    onClick = {
                        likesCount++
                        floatingHearts = floatingHearts + (1..1000).random()
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, NeonPink, CircleShape)
                        .testTag("like_burst_btn")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "💖", fontSize = 18.sp)
                        Text(
                            text = "${likesCount / 1000}k",
                            fontSize = 8.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Gift Button
                IconButton(
                    onClick = { showGiftSheet = true },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(NeonMagenta, CoinGold)))
                        .testTag("live_gift_btn")
                ) {
                    Text(text = "🎁", fontSize = 22.sp)
                }
            }
        }

        // Gift Bottom Sheet
        if (showGiftSheet) {
            GiftBottomSheet(
                currentCoins = currentUser.demoCoins,
                targetName = room.host.name,
                onDismiss = { showGiftSheet = false },
                onSendGift = { gift, combo ->
                    onSendGift(gift, combo, room.host)
                    pkLeftScore += (gift.coinPrice * combo).toInt()
                    latestGiftAnnouncement = Pair(gift, combo)
                    messages = messages + ChatMessage(
                        id = "gift_${System.currentTimeMillis()}",
                        sender = currentUser,
                        message = "sent ${gift.name} ${gift.iconEmoji} x$combo to ${room.host.name}!",
                        timestamp = "Now",
                        isSystem = true,
                        gift = gift,
                        giftCombo = combo
                    )
                    showGiftSheet = false
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                },
                onGetFreeDemoCoins = onAddCoins
            )
        }
    }
}

@Composable
fun PKBattleHeader(
    leftHost: User,
    rightHost: User,
    leftScore: Int,
    rightScore: Int
) {
    val total = (leftScore + rightScore).coerceAtLeast(1)
    val leftFraction = (leftScore.toFloat() / total).coerceIn(0.15f, 0.85f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${leftHost.name}: $leftScore",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )

            Text(
                text = "⚡ PK BATTLE 🥊",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = CoinGold
            )

            Text(
                text = "${rightHost.name}: $rightScore",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = NeonPink
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Battle Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(leftFraction)
                    .fillMaxHeight()
                    .background(NeonCyan)
            )
            Box(
                modifier = Modifier
                    .weight(1f - leftFraction)
                    .fillMaxHeight()
                    .background(NeonPink)
            )
        }
    }
}

@Composable
fun FloatingHeartItem() {
    val infiniteTransition = rememberInfiniteTransition(label = "heart")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -350f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetY"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp, end = 30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = listOf("💖", "🔥", "✨", "💜", "👑").random(),
            fontSize = 24.sp,
            modifier = Modifier
                .offset(y = offsetY.dp)
                .background(Color.Transparent)
        )
    }
}
