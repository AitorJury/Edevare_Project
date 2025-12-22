package com.edevare.shared.entitiesDTO

import com.edevare.shared.enums.TeacherModality

data class TeacherProfileDTO(
    val idUser: Long,
    val descriptor: String?,
    val hourlyRate: Double?,
    val modality: TeacherModality
)
