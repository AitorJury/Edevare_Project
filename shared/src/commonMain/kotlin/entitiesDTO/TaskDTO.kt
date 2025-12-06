package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(
    val idTask: Long? = null, //Se crea con valor nulo
    val title : String,
    val priority: String,
    val completed: Boolean,
    val createdAt : String? = null, //Se crea con valor nulo, y String para simplificar fechas en JSON
    val idUser : Long //Solo se le pasa el id para saber quien es el responsable de la tarea

)
