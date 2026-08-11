package com.example.mvikmpserver.model

import kotlinx.serialization.Serializable

@Serializable
data class OperacionDto(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val imagenUrl: String? = null,
    val tipo: String? = null,
    val autor: String? = null,
    val fechaCreacion: Long,
)
