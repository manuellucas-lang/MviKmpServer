package com.example.mvikmpserver.db

import org.jetbrains.exposed.sql.Table

object OperacionesTable : Table("operaciones") {
    val id = long("id").autoIncrement()
    val titulo = text("titulo")
    val descripcion = text("descripcion")
    val imagenUrl = text("imagen_url").nullable()
    val tipo = text("tipo").nullable()
    val autor = text("autor").nullable()
    val fechaCreacion = long("fecha_creacion")
    val guardada = bool("guardada").default(false)

    override val primaryKey = PrimaryKey(id)
}
