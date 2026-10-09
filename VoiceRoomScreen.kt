package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.ui.components.FloatingGiftAnnouncement
import com.example.ui.components.GiftBottomSheet
import com.example.ui.components.VoiceSpeakingRipple
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRoomScreen(
    room: LiveRoom,
    currentUser: User,
    onClose: () -> Unit,
    onSendGift: (GiftItem, Int, User) -> Unit,
    onAddCoins: () -> Unit
) {
    var seats by remember {
        mutableStateOf(
            if (room.seats.isNotEmpty()) room.seats else MockDataProvider.create12Seats(7)
        )
    }

    var isMyMicMuted by remember { mutableStateOf(false) }
    var mySeatNumber by remember { mutableStateOf<Int?>(null) }
    var showGiftSheet by remember { mutableStateOf(false) }
    var giftTargetUser by remember { mutableStateOf<User>(room.host) }

    // Live messages
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("m1", room.host, "Welcome to the 12-Seat Party Room! Tap any empty seat to grab the mic! 🎙️", "Just now", true),
                ChatMessage("m2", MockDataProvider.users[1], "Hello everyone! Sound check on seat 2!", "Just now"),
                ChatMessage("m3", MockDataProvider.users[3], "Vibing to the background music 🔥", "Just now")
            )
        )
    }

    var textInput by remember { mutableStateOf("") }
    var latestGiftAnnouncement by remember { mutableStateOf<Pair<GiftItem, Int>?>(null) }
    var soundEffectText by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Periodically toggle speaking indicator on random occupied seat to make it feel alive!
    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            seats = seats.map { seat ->
                if (seat.user != null && !seat.isMuted) {
                    seat.copy(isSpeaking = (1..100).random() > 60)
                } else seat
            }
        }
    }

    // Effect to hide gift banner
    LaunchedEffect(latestGiftAnnouncement) {
        if (latestGiftAnnouncement != null) {
            delay(4000)
            latestGiftAnnouncement = null
        }
    }

    // Effect to hide sound effect
    LaunchedEffect(soundEffectText) {
        if (soundEffectText != null) {
            delay(2000)
            soundEffectText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F0B2E),
                        DarkBg,
                        Color(0xFF131038)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Room Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Room Title & Host Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DarkSurfaceVariant.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = room.host.avatarEmoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = room.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(NeonCyan)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${room.viewerCount} online",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Action buttons: Share and Leave Room
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = {
                            soundEffectText = "👏 Room Shared with Friends!"
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LiveRed.copy(alpha = 0.8f))
                            .testTag("exit_voice_room_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Room", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Host Podium (Seat #0 / Central Stage)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        giftTargetUser = room.host
                        showGiftSheet = true
                    }
                ) {
                    VoiceSpeakingRipple(isSpeaking = true) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DarkCard)
                                .border(
                                    2.5.dp,
                                    Brush.linearGradient(listOf(CoinGold, NeonMagenta)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = room.host.avatarEmoji, fontSize = 30.sp)

                            // Crown Host Badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-8).dp)
                            ) {
                                Text(text = "👑", fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CoinGold)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "HOST",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = room.host.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // 12-SEATS GRID (3 rows of 4 seats)
            Text(
                text = "12 Voice Seats",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val seatChunks = seats.chunked(4) // 3 rows of 4 = 12 seats
                seatChunks.forEach { rowSeats ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        rowSeats.forEach { seat ->
                            VoiceSeatItem(
                                seat = seat,
                                isCurrentUser = mySeatNumber == seat.seatNumber,
                                onClick = {
                                    if (seat.isLocked) {
                                        soundEffectText = "🔒 Seat #${seat.seatNumber} is reserved!"
                                    } else if (seat.user != null) {
                                        // Occupied seat - send gift or interact
                                        giftTargetUser = seat.user
                                        showGiftSheet = true
                                    } else {
                                        // Take vacant seat!
                                        if (mySeatNumber == null) {
                                            mySeatNumber = seat.seatNumber
                                            seats = seats.map { s ->
                                                if (s.seatNumber == seat.seatNumber) {
                                                    s.copy(user = currentUser, isSpeaking = false)
                                                } else s
                                            }
                                            soundEffectText = "🎙️ You took Seat #${seat.seatNumber}!"
                                        } else {
                                            soundEffectText = "You are already on Seat #$mySeatNumber"
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sound Effects Trigger Pill Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "👏 Applause" to "👏 Crowd Applauds!",
                    "🎉 Cheers" to "🎉 Huge Cheers from the room!",
                    "📢 Horn" to "📢 DJ Airhorn Blast!",
                    "😂 Laugh" to "😂 Room Laughter!"
                ).forEach { (label, soundText) ->
                    Surface(
                        onClick = {
                            soundEffectText = soundText
                            messages = messages + ChatMessage(
                                id = "fx_${System.currentTimeMillis()}",
                                sender = currentUser,
                                message = soundText,
                                timestamp = "Now",
                                isSystem = true
                            )
                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // In-room Chat Stream
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkCard.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            if (msg.isSystem) {
                                Text(
                                    text = "📢 ",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = msg.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NeonCyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Text(
                                    text = "${msg.sender.avatarEmoji} ${msg.sender.name}: ",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (msg.sender.id == currentUser.id) NeonMagenta else NeonPurpleLight
                                )
                                Text(
                                    text = msg.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Control Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chat Input Field
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Say something...", fontSize = 12.sp, color = TextTertiary) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("voice_chat_input"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = NeonPurple
                        ),
                        trailingIcon = {
                            if (textInput.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        messages = messages + ChatMessage(
                                            id = "msg_${System.currentTimeMillis()}",
                                            sender = currentUser,
                                            message = textInput.trim(),
                                            timestamp = "Now"
                                        )
                                        textInput = ""
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    },
                                    modifier = Modifier.testTag("send_voice_chat_btn")
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = "Send", tint = NeonPurple)
                                }
                            }
                        }
                    )

                    // Leave seat button if on seat
                    if (mySeatNumber != null) {
                        IconButton(
                            onClick = {
                                seats = seats.map { s ->
                                    if (s.seatNumber == mySeatNumber) s.copy(user = null, isSpeaking = false) else s
                                }
                                mySeatNumber = null
                                soundEffectText = "Left mic seat"
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(LiveRed.copy(alpha = 0.2f))
                                .border(1.dp, LiveRed, CircleShape)
                        ) {
                            Icon(Icons.Default.CallEnd, contentDescription = "Leave Seat", tint = LiveRed, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Mic Mute / Unmute Button
                    IconButton(
                        onClick = {
                            isMyMicMuted = !isMyMicMuted
                            if (mySeatNumber != null) {
                                seats = seats.map { s ->
                                    if (s.seatNumber == mySeatNumber) s.copy(isMuted = isMyMicMuted) else s
                                }
                            }
                            soundEffectText = if (isMyMicMuted) "🔇 Mic Muted" else "🎙️ Mic Active"
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (isMyMicMuted) DarkBorder else NeonPurple)
                            .testTag("mic_toggle_btn")
                    ) {
                        Icon(
                            if (isMyMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mic toggle",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Gift Button
                    IconButton(
                        onClick = {
                            giftTargetUser = room.host
                            showGiftSheet = true
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(NeonMagenta, CoinGold))
                            )
                            .testTag("open_gift_sheet_btn")
                    ) {
                        Text(text = "🎁", fontSize = 20.sp)
                    }
                }
            }
        }

        // Overlay: Floating Sound Effect Pill
        soundEffectText?.let { effect ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.85f))
                    .border(1.5.dp, NeonPurple, RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = effect,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Overlay: Floating Gift Announcement
        latestGiftAnnouncement?.let { (gift, combo) ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 50.dp)
            ) {
                FloatingGiftAnnouncement(
                    senderName = currentUser.name,
                    receiverName = giftTargetUser.name,
                    gift = gift,
                    combo = combo,
                    visible = true
                )
            }
        }

        // Gift Bottom Sheet
        if (showGiftSheet) {
            GiftBottomSheet(
                currentCoins = currentUser.demoCoins,
                targetName = giftTargetUser.name,
                onDismiss = { showGiftSheet = false },
                onSendGift = { gift, combo ->
                    onSendGift(gift, combo, giftTargetUser)
                    latestGiftAnnouncement = Pair(gift, combo)
                    messages = messages + ChatMessage(
                        id = "gift_${System.currentTimeMillis()}",
                        sender = currentUser,
                        message = "sent ${gift.name} ${gift.iconEmoji} x$combo to ${giftTargetUser.name}!",
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
fun VoiceSeatItem(
    seat: Seat,
    isCurrentUser: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
            .testTag("seat_${seat.seatNumber}")
    ) {
        VoiceSpeakingRipple(isSpeaking = seat.isSpeaking && seat.user != null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (seat.user != null) DarkCard else DarkSurfaceVariant)
                    .border(
                        1.5.dp,
                        when {
                            isCurrentUser -> NeonMagenta
                            seat.isSpeaking -> NeonCyan
                            seat.user != null -> NeonPurple
                            seat.isLocked -> DarkBorder
                            else -> DarkBorder.copy(alpha = 0.6f)
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    seat.isLocked -> {
                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = TextTertiary, modifier = Modifier.size(18.dp))
                    }
                    seat.user != null -> {
                        Text(text = seat.user.avatarEmoji, fontSize = 24.sp)
                    }
                    else -> {
                        Icon(Icons.Default.Add, contentDescription = "Take Seat", tint = NeonPurpleLight, modifier = Modifier.size(20.dp))
                    }
                }

                // Muted badge
                if (seat.isMuted && seat.user != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(LiveRed)
                            .border(1.dp, DarkBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MicOff, contentDescription = "Muted", tint = Color.White, modifier = Modifier.size(10.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Seat Number & Name Label
        Text(
            text = seat.user?.name?.substringBefore(" ") ?: "Seat #${seat.seatNumber}",
            style = MaterialTheme.typography.labelSmall,
            color = if (isCurrentUser) NeonMagenta else if (seat.user != null) TextPrimary else TextSecondary,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
