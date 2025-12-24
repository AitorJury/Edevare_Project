package com.edevare.shared.entitiesDTO

import com.edevare.shared.enums.TeacherModality
import kotlinx.serialization.Serializable


/**
 * DTO para enviar datos combinados de oferta de clase
 * Envia tanto datos de la clase
 * Nombre de la materia
 * Precio del profesor
 */
@Serializable
data class OfferResponseDTO(
    val id: Long,
    val subjectName: String,
    val title: String,
    val academicLevel: String?,
    val teacherId: Long,
    val price: Double,
    val modality: TeacherModality,
    val description: String?
)
