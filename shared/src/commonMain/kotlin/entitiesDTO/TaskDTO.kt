package com.edevare.shared.entitiesDTO

import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(
    val id_task: Long? = null, //Se crea con valor nulo
    val title : String,
    val priority: String,
    val is_complete: Boolean,
    val created_at : String? = null, //Se crea con valor nulo, y String para simplificar fechas en JSON
    val id_user : Long //Solo se le pasa el id para saber quien es el responsable de la tarea

)
