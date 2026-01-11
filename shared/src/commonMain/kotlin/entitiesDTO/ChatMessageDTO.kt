package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO(
    val id: Long? = null,
    val senderId: String, //Quien envia el mensaje
    val receiverId: String, // Quien lo recibe
    val bodyMessage: String,
    val timestamp: Long
)