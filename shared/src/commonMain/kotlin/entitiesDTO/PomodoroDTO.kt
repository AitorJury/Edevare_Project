package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroDTO(
    val idSession: Long? = null,
    val idUser: Long,
    val durationMinutes: Int,
    val sessionType: String, // "WORK" o "BREAK"
    val timestamp: String? = null,
    val microBreakSuggestion: String? = null
)