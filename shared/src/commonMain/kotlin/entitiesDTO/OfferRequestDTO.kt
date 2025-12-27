package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

/**
 * Este DTO define los datos necesarios para crear una oferta de clase
 * Quien la ofrece y que materia imparte
 */
@Serializable
data class OfferRequestDTO(
    val idTeacher: Long,
    val idSubject: Long,
    val title: String,
    val price: Double,
    val description: String? = null
)
