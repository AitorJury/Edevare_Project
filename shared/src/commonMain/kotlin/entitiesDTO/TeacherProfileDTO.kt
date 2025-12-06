package com.edevare.shared.DTOsEntities

data class TeacherProfileDTO(
    val idUser: Long,
    val descriptor: String?,
    val hourlyRate: Double?,
    val modality: String?
)
