package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val idChat: Long? = null,
    val idStudent: Long,
    val idTeacher: Long,
    val otherUserName: String? = null,
    val lastMessage: String? = null,
    val lastMessageDate: String? = null
)