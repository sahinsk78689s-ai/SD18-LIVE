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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataProvider
import com.example.model.DirectConversation
import com.example.model.GiftItem
import com.example.model.User
import com.example.ui.components.GiftBottomSheet
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    currentUser: User,
    onStartCall: (User, Boolean) -> Unit, // (peer, isVideo)
    onAddCoins: () -> Unit
) {
    var conversations by remember { mutableStateOf(MockDataProvider.directConversations) }
    var activeConversation by remember { mutableStateOf<DirectConversation?>(null) }

    if (activeConversation != null) {
        DirectChatDetailScreen(
            conversation = activeConversation!!,
            currentUser = currentUser,
            onBack = { activeConversation = null },
            onStartCall = { isVideo -> onStartCall(activeConversation!!.peerUser, isVideo) },
            onAddCoins = onAddCoins
        )
    } else {
        ConversationListScreen(
            conversations = conversations,
            onSelectConversation = { activeConversation = it }
        )
    }
}

@Composable
fun ConversationListScreen(
    conversations: List<DirectConversation>,
    onSelectConversation: (DirectConversation) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Search & Filter header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Messages & Direct Chats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            IconButton(onClick = { }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = TextSecondary)
            }
        }

        // Online hosts / friends strip
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Text(
                    text = "Active Now",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    conversations.forEach { conv ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onSelectConversation(conv) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant)
                                    .border(1.5.dp, NeonPurple, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = conv.peerUser.avatarEmoji, fontSize = 24.sp)
                                if (conv.isOnline) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(SuccessGreen)
                                            .border(1.5.dp, DarkBg, CircleShape)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = conv.peerUser.name.substringBefore(" "),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = DarkBorder,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            items(conversations) { conv ->
                ConversationItem(
                    conversation = conv,
                    onClick = { onSelectConversation(conv) }
                )
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: DirectConversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant)
                .border(1.5.dp, DarkBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = conversation.peerUser.avatarEmoji, fontSize = 26.sp)
            if (conversation.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                        .border(1.5.dp, DarkBg, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.peerUser.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = conversation.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(NeonMagenta)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${conversation.unreadCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DirectChatDetailScreen(
    conversation: DirectConversation,
    currentUser: User,
    onBack: () -> Unit,
    onStartCall: (Boolean) -> Unit, // isVideo
    onAddCoins: () -> Unit
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                "Hey Sujan! Thanks for tuning into my voice party! 🎶" to false,
                "Loved the music and the rap cypher! 🔥" to true,
                conversation.lastMessage to false
            )
        )
    }

    var textInput by remember { mutableStateOf("") }
    var showGiftSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = conversation.peerUser.avatarEmoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = conversation.peerUser.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (conversation.isOnline) "🟢 Online" else "Offline",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (conversation.isOnline) SuccessGreen else TextTertiary
                        )
                    }
                }

                // Call Actions: Audio Call & Video Call
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onStartCall(false) },
                        modifier = Modifier.testTag("start_audio_call_btn")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Audio Call", tint = NeonCyan)
                    }
                    IconButton(
                        onClick = { onStartCall(true) },
                        modifier = Modifier.testTag("start_video_call_btn")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = NeonMagenta)
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { (text, isMe) ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 2.dp,
                                    bottomEnd = if (isMe) 2.dp else 16.dp
                                )
                            )
                            .background(if (isMe) NeonPurple else DarkSurfaceVariant)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Bottom Input Row with Gift Send
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Gift in DM button
                IconButton(
                    onClick = { showGiftSheet = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("dm_gift_btn")
                ) {
                    Text(text = "🎁", fontSize = 18.sp)
                }

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Write a message...", color = TextTertiary, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    trailingIcon = {
                        if (textInput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    messages = messages + (textInput.trim() to true)
                                    textInput = ""
                                }
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = NeonPurple)
                            }
                        }
                    }
                )
            }
        }

        // Gift Bottom Sheet in DM
        if (showGiftSheet) {
            GiftBottomSheet(
                currentCoins = currentUser.demoCoins,
                targetName = conversation.peerUser.name,
                onDismiss = { showGiftSheet = false },
                onSendGift = { gift, combo ->
                    messages = messages + ("Sent you ${gift.name} ${gift.iconEmoji} x$combo! 🪙" to true)
                    showGiftSheet = false
                },
                onGetFreeDemoCoins = onAddCoins
            )
        }
    }
}
