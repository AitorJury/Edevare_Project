package com.edevare.shared.entitiesDTO

import com.edevare.shared.enums.TeacherModality
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal


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
    val academicLevel: String?,
    val teacherId: Long,
    @Contextual
    val hourlyRate: BigDecimal,
    val modality: TeacherModality,
    val description: String?
)
