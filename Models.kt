package com.example.model

enum class UserRole {
    USER,
    HOST,
    AGENCY_BOSS,
    SUPER_ADMIN
}

data class User(
    val id: String,
    val name: String,
    val handle: String,
    val avatarEmoji: String,
    val role: UserRole = UserRole.USER,
    val demoCoins: Long = 25000,
    val diamonds: Long = 1200,
    val level: Int = 18,
    val isVip: Boolean = true,
    val agencyId: String? = null,
    val isBanned: Boolean = false
)

data class Seat(
    val seatNumber: Int, // 1 to 12
    val user: User? = null,
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
    val isSpeaking: Boolean = false
)

enum class RoomType {
    VOICE_PARTY_12,
    LIVE_VIDEO,
    PK_BATTLE
}

data class LiveRoom(
    val id: String,
    val title: String,
    val host: User,
    val type: RoomType,
    val viewerCount: Int,
    val category: String,
    val tags: List<String>,
    val seats: List<Seat> = emptyList(),
    val pkScoreLeft: Int = 12400,
    val pkScoreRight: Int = 9850,
    val opponentHost: User? = null
)

data class GiftItem(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val coinPrice: Long,
    val category: String,
    val description: String,
    val animationColor: Long
)

data class ChatMessage(
    val id: String,
    val sender: User,
    val message: String,
    val timestamp: String,
    val isSystem: Boolean = false,
    val gift: GiftItem? = null,
    val giftCombo: Int = 1
)

data class Agency(
    val id: String,
    val name: String,
    val code: String,
    val managerName: String,
    val contactEmail: String,
    val hostCount: Int,
    val commissionPercent: Int,
    val totalRevenueCoins: Long,
    val status: String // "Approved", "Pending", "Active"
)

data class HostMember(
    val id: String,
    val name: String,
    val avatarEmoji: String,
    val status: String, // "Live", "Offline"
    val hoursStreamed: Float,
    val coinsEarned: Long,
    val peakViewers: Int
)

data class DirectConversation(
    val id: String,
    val peerUser: User,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

data class TransactionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Long,
    val isCredit: Boolean,
    val timestamp: String
)
