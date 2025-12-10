package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class UserRequestDTO(
    val email: String,
    val password: String,
    val roles: List<String>

)
