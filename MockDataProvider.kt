package com.example.data

import com.example.model.*

object MockDataProvider {

    val currentUser = User(
        id = "user_me",
        name = "Sujan Star",
        handle = "@sujan_sd18",
        avatarEmoji = "👑",
        role = UserRole.USER,
        demoCoins = 50000,
        diamonds = 3400,
        level = 24,
        isVip = true
    )

    val users = listOf(
        User("u1", "Elena Vance", "@elena_live", "✨", UserRole.HOST, 120000, 15400, 32, true),
        User("u2", "DJ Pulse", "@djpulse", "🎧", UserRole.HOST, 85000, 9200, 28, false),
        User("u3", "Maya Lin", "@maya_sing", "🎤", UserRole.HOST, 94000, 11000, 30, true),
        User("u4", "Kenji Neon", "@kenji_jp", "⚡", UserRole.USER, 35000, 2100, 19, false),
        User("u5", "Sophia Cruz", "@sophia_c", "🌸", UserRole.HOST, 140000, 18500, 35, true),
        User("u6", "Leo Blade", "@leoblade", "🔥", UserRole.USER, 22000, 800, 15, false),
        User("u7", "Zara Queen", "@zara_q", "💎", UserRole.AGENCY_BOSS, 450000, 89000, 48, true),
        User("u8", "Cyber Sam", "@cybersam", "🚀", UserRole.SUPER_ADMIN, 999999, 999999, 99, true),
        User("u9", "Aria Rose", "@ariarose", "🌹", UserRole.HOST, 77000, 7800, 26, true),
        User("u10", "GhostRider", "@ghost_r", "🏍️", UserRole.USER, 18000, 600, 14, false),
        User("u11", "Chloe Beats", "@chloe_b", "🎵", UserRole.USER, 41000, 1900, 21, true),
        User("u12", "Felix Sun", "@felix_sun", "☀️", UserRole.USER, 29000, 1200, 17, false)
    )

    val gifts = listOf(
        GiftItem("g1", "Red Rose", "🌹", 10, "Popular", "Express sweet affection", 0xFFF43F5E),
        GiftItem("g2", "Love Heart", "💖", 50, "Popular", "Sparkling heart vibes", 0xFFEC4899),
        GiftItem("g3", "Cyber Soda", "🥤", 100, "Popular", "Cool refreshment", 0xFF06B6D4),
        GiftItem("g4", "Golden Crown", "👑", 500, "Luxury", "Royal honour for the host", 0xFFF59E0B),
        GiftItem("g5", "Diamond Ring", "💍", 1000, "Luxury", "Shine with elegance", 0xFF38BDF8),
        GiftItem("g6", "Neon Sports Car", "🏎️", 2500, "Special", "Vroom into the party!", 0xFF8B5CF6),
        GiftItem("g7", "Cyber Yacht", "🛥️", 5000, "Special", "Cruising the neon sea", 0xFF3B82F6),
        GiftItem("g8", "Super Rocket", "🚀", 10000, "Legendary", "Blast off to the stars!", 0xFFF97316),
        GiftItem("g9", "Galaxy Castle", "🏰", 25000, "Legendary", "Ultimate prestige palace", 0xFFA855F7)
    )

    fun create12Seats(occupiedCount: Int = 8): List<Seat> {
        val seatUsers = listOf(
            users[0], users[1], users[2], users[3],
            users[4], users[5], users[8], users[9],
            users[10], users[11], null, null
        )

        return (1..12).map { index ->
            val u = if (index - 1 < occupiedCount) seatUsers.getOrNull(index - 1) else null
            Seat(
                seatNumber = index,
                user = u,
                isMuted = index % 4 == 0,
                isLocked = index == 12, // 12th seat reserved/locked
                isSpeaking = index == 1 || index == 3
            )
        }
    }

    val liveRooms: List<LiveRoom> = listOf(
        LiveRoom(
            id = "room_1",
            title = "SD18 Midnight Chill & Beats 🎶 [12 Mics Open]",
            host = users[0],
            type = RoomType.VOICE_PARTY_12,
            viewerCount = 1420,
            category = "Voice Party",
            tags = listOf("12 Seats", "Chat", "Chill", "Lofi"),
            seats = create12Seats(7)
        ),
        LiveRoom(
            id = "room_2",
            title = "🔥 12-Seat Karaoke & Freestyle Rap Lounge",
            host = users[2],
            type = RoomType.VOICE_PARTY_12,
            viewerCount = 2890,
            category = "Voice Party",
            tags = listOf("12 Seats", "Singing", "Hot", "Music"),
            seats = create12Seats(9)
        ),
        LiveRoom(
            id = "room_3",
            title = "✨ Neon Tokyo Night Stream & DJ Set",
            host = users[1],
            type = RoomType.LIVE_VIDEO,
            viewerCount = 3750,
            category = "Live Video",
            tags = listOf("DJ", "EDM", "Dancing", "4K")
        ),
        LiveRoom(
            id = "room_4",
            title = "⚡ EPIC PK BATTLE: Elena vs Sophia! 🥊",
            host = users[0],
            type = RoomType.PK_BATTLE,
            viewerCount = 5620,
            category = "PK Battle",
            tags = listOf("PK Battle", "Gifts x2", "High Voltage"),
            pkScoreLeft = 32800,
            pkScoreRight = 29400,
            opponentHost = users[4]
        ),
        LiveRoom(
            id = "room_5",
            title = "🌙 Late Night Confessions & Truths [12 Seats]",
            host = users[4],
            type = RoomType.VOICE_PARTY_12,
            viewerCount = 940,
            category = "Voice Party",
            tags = listOf("12 Seats", "Dating", "Deep Talk"),
            seats = create12Seats(6)
        ),
        LiveRoom(
            id = "room_6",
            title = "🎮 Mobile Gaming Arena: MLBB & Free Fire",
            host = users[5],
            type = RoomType.LIVE_VIDEO,
            viewerCount = 1880,
            category = "Live Video",
            tags = listOf("Gaming", "Custom Room", "Giveaway")
        )
    )

    val directConversations = listOf(
        DirectConversation(
            id = "conv_1",
            peerUser = users[0],
            lastMessage = "Thanks so much for the Sports Car in the party room! 🏎️💖",
            timestamp = "12:45 PM",
            unreadCount = 2,
            isOnline = true
        ),
        DirectConversation(
            id = "conv_2",
            peerUser = users[6],
            lastMessage = "Welcome to StarWave Agency! Check your host contract details.",
            timestamp = "Yesterday",
            unreadCount = 0,
            isOnline = true
        ),
        DirectConversation(
            id = "conv_3",
            peerUser = users[2],
            lastMessage = "Are you joining seat #4 for the rap cypher tonight?",
            timestamp = "Oct 8",
            unreadCount = 1,
            isOnline = false
        ),
        DirectConversation(
            id = "conv_4",
            peerUser = users[1],
            lastMessage = "Audio call ended (14 mins)",
            timestamp = "Oct 7",
            unreadCount = 0,
            isOnline = true
        )
    )

    val sampleAgencies = listOf(
        Agency(
            id = "ag_1",
            name = "StarWave Entertainment",
            code = "SW-9988",
            managerName = "Zara Queen",
            contactEmail = "zara@starwave.live",
            hostCount = 28,
            commissionPercent = 20,
            totalRevenueCoins = 1850000,
            status = "Approved"
        ),
        Agency(
            id = "ag_2",
            name = "Neon Nexus Guild",
            code = "NNG-1024",
            managerName = "Marcus Ray",
            contactEmail = "marcus@neonnexus.io",
            hostCount = 15,
            commissionPercent = 18,
            totalRevenueCoins = 920000,
            status = "Active"
        )
    )

    val sampleAgencyHosts = listOf(
        HostMember("h1", "Elena Vance", "✨", "Live", 42.5f, 280000, 4200),
        HostMember("h2", "DJ Pulse", "🎧", "Live", 31.0f, 195000, 3100),
        HostMember("h3", "Sophia Cruz", "🌸", "Offline", 38.2f, 310000, 5600),
        HostMember("h4", "Maya Lin", "🎤", "Offline", 25.5f, 150000, 2400)
    )

    val sampleTransactions = listOf(
        TransactionItem("tx_1", "Daily Check-in Reward", "Free demo coins credited", 10000, true, "Today 08:00 AM"),
        TransactionItem("tx_2", "Sent Gift: Neon Sports Car", "To Elena Vance in Room #1", 2500, false, "Today 11:22 AM"),
        TransactionItem("tx_3", "Voice Room Tip Received", "From Kenji Neon on Seat #3", 500, true, "Yesterday 09:14 PM"),
        TransactionItem("tx_4", "Sent Gift: Red Rose x10", "To Maya Lin in Room #2", 100, false, "Oct 7 07:30 PM"),
        TransactionItem("tx_5", "Welcome Bonus", "Initial test coins grant", 50000, true, "Oct 5 10:00 AM")
    )
}
