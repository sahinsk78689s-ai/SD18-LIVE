package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.model.LiveRoom
import com.example.model.RoomType
import com.example.model.UserRole
import com.example.ui.components.LiveRoomCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    rooms: List<LiveRoom>,
    onSelectRoom: (LiveRoom) -> Unit,
    onCreateRoom: () -> Unit,
    onNavigateToAgency: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("🔥 Trending", "🎙️ 12 Seats", "🔴 Live Video", "⚔️ PK Battles")

    val filteredRooms = remember(selectedTab, rooms) {
        when (selectedTab) {
            1 -> rooms.filter { it.type == RoomType.VOICE_PARTY_12 }
            2 -> rooms.filter { it.type == RoomType.LIVE_VIDEO }
            3 -> rooms.filter { it.type == RoomType.PK_BATTLE }
            else -> rooms
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Banner Card
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF4C1D95),
                                Color(0xFF831843),
                                Color(0xFF1E1B4B)
                            )
                        )
                    )
                    .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CoinGold)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SD18 FESTIVAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                        Text(
                            text = "12-Mic Voice Carnival",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "Voice Party Rooms with 12 Seats",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Jump on mic, battle in PK, & drop test gifts!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onCreateRoom,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("banner_create_room_btn")
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open 12-Seat Room", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onNavigateToAgency,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("banner_agency_btn")
                        ) {
                            Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agency Hub", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Active Streamers Avatars Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Star Hosts Online",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonPurpleLight,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(MockDataProvider.users.filter { it.role == UserRole.HOST }) { host ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                // Find or open host room
                                val hostRoom = rooms.find { it.host.id == host.id } ?: rooms.first()
                                onSelectRoom(hostRoom)
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(DarkCard)
                                    .border(
                                        2.dp,
                                        Brush.linearGradient(listOf(NeonPurple, NeonMagenta)),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = host.avatarEmoji, fontSize = 24.sp)

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(LiveRed)
                                        .border(2.dp, DarkBg, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = host.name.substringBefore(" "),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Filter Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkBg,
                contentColor = NeonPurple,
                edgePadding = 16.dp,
                divider = {},
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NeonPurpleLight else TextSecondary,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.testTag("tab_$index")
                    )
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${tabs[selectedTab]} (${filteredRooms.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Auto-Refresh ⚡",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }

        // Room Grid (2 columns)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filteredRooms.chunked(2).forEach { rowRooms ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowRooms.forEach { room ->
                            LiveRoomCard(
                                room = room,
                                onClick = { onSelectRoom(room) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowRooms.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
