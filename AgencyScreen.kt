package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.Agency
import com.example.model.HostMember
import com.example.ui.theme.*

@Composable
fun AgencyScreen(
    onBack: () -> Unit
) {
    var agencies by remember { mutableStateOf(MockDataProvider.sampleAgencies) }
    var hostList by remember { mutableStateOf(MockDataProvider.sampleAgencyHosts) }
    var isRegistered by remember { mutableStateOf(true) }
    var currentAgency by remember { mutableStateOf(agencies.first()) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Text(
                        text = "Agency Hub & Host Management",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Button(
                    onClick = { showRegisterDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("register_agency_top_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Register Agency", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Agency Overview Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1E1B4B),
                                    Color(0xFF4C1D95),
                                    Color(0xFF311042)
                                )
                            )
                        )
                        .border(1.5.dp, NeonPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentAgency.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SuccessGreen)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = currentAgency.status.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                Text(
                                    text = "Agency Code: ${currentAgency.code} • Mgr: ${currentAgency.managerName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏢", fontSize = 24.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Signed Hosts", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("${currentAgency.hostCount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NeonCyan)
                            }

                            Column {
                                Text("Commission", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("${currentAgency.commissionPercent}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CoinGold)
                            }

                            Column {
                                Text("Monthly Pool", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("🪙 ${(currentAgency.totalRevenueCoins / 1000)}k", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NeonMagenta)
                            }
                        }
                    }
                }
            }

            // Quick Agency Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val newHost = HostMember(
                                id = "h_${System.currentTimeMillis()}",
                                name = "Star Talent #${hostList.size + 1}",
                                avatarEmoji = listOf("🌟", "🎵", "🎸", "💫").random(),
                                status = "Live",
                                hoursStreamed = 12.0f,
                                coinsEarned = 45000,
                                peakViewers = 1200
                            )
                            hostList = hostList + newHost
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("invite_host_btn")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Recruit Host", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Invite Link", color = TextPrimary)
                    }
                }
            }

            // Managed Hosts Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Managed Star Hosts (${hostList.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Real-Time Telemetry 📈",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }
            }

            // Managed Hosts Items
            items(hostList) { host ->
                ManagedHostCard(host = host)
            }
        }

        // Agency Registration Dialog
        if (showRegisterDialog) {
            AgencyRegistrationDialog(
                onDismiss = { showRegisterDialog = false },
                onSubmit = { name, code, manager, email, rate ->
                    val newAgency = Agency(
                        id = "ag_${System.currentTimeMillis()}",
                        name = name,
                        code = code,
                        managerName = manager,
                        contactEmail = email,
                        hostCount = 1,
                        commissionPercent = rate,
                        totalRevenueCoins = 50000,
                        status = "Active"
                    )
                    agencies = agencies + newAgency
                    currentAgency = newAgency
                    showRegisterDialog = false
                }
            )
        }
    }
}

@Composable
fun ManagedHostCard(host: HostMember) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .border(1.5.dp, if (host.status == "Live") LiveRed else DarkBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = host.avatarEmoji, fontSize = 22.sp)
                if (host.status == "Live") {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(LiveRed)
                            .border(1.dp, DarkBg, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = host.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (host.status == "Live") LiveRed.copy(alpha = 0.2f) else DarkBorder)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = host.status,
                            fontSize = 9.sp,
                            color = if (host.status == "Live") LiveRed else TextTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "⏱️ ${host.hoursStreamed}h",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "🪙 ${(host.coinsEarned / 1000)}k coins",
                        style = MaterialTheme.typography.labelSmall,
                        color = CoinGold
                    )
                    Text(
                        text = "👥 ${host.peakViewers} peak",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }
            }
        }
    }
}

@Composable
fun AgencyRegistrationDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, Int) -> Unit
) {
    var agencyName by remember { mutableStateOf("") }
    var agencyCode by remember { mutableStateOf("AG-${(1000..9999).random()}") }
    var managerName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var commissionRate by remember { mutableStateOf(20) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = "Register Agency Partner",
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
                    text = "Manage hosts, host rooms, and revenue splits in SD18 LIVE.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = agencyName,
                    onValueChange = { agencyName = it },
                    label = { Text("Agency Name") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_agency_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = managerName,
                    onValueChange = { managerName = it },
                    label = { Text("Manager Name") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_agency_manager_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Contact Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text(
                    text = "Commission Split: $commissionRate% (Agency) / ${100 - commissionRate}% (Host)",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (agencyName.isNotBlank() && managerName.isNotBlank()) {
                        onSubmit(agencyName, agencyCode, managerName, email, commissionRate)
                    }
                },
                enabled = agencyName.isNotBlank() && managerName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("submit_agency_btn")
            ) {
                Text("Register Agency", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
