package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO(
    val id: Long? = null,
    val senderId: Long, //Quien envia el mensaje
    val receiverId: Long, // Quien lo recibe
    val contentMessage: String,
    val timestamp: String
)