package com.edevare.shared

// Para que Ktor pueda convertir esto a JSON
import kotlinx.serialization.Serializable

// DTO de entrada para el Registro (cliente envía al servidor)
@Serializable
data class RegistroRequest(
    val email: String,
    val password: String,
    val rol: String
)

// DTO de respuesta (çservidor devuelve)
@Serializable
data class AuthResponse(
    val userId: Long,
    // JWT
    val token: String,
    val role: String
)

//DTO para iniciar sesion (solo con el email y la contraseña)
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)