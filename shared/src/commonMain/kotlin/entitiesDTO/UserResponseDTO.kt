package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val id: Long,
    val email: String,
    val roles : List<String> //Se envia solo el nombre del rol no el objeto ROL entero
)