package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO(
    val id: Long? = null,
    val chatId: Long? = null,
    val senderId: Long = 0, //Quien envia el mensaje
    val receiverId: Long = 0, // Quien lo recibe
    val contentMessage: String = "",
    val timestamp: String = ""
)