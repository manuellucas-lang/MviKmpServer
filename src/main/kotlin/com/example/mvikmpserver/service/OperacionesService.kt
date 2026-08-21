package com.example.mvikmpserver.service

import com.example.mvikmpserver.db.OperacionesTable
import com.example.mvikmpserver.model.CreateOperacionRequest
import com.example.mvikmpserver.model.OperacionDto
import com.example.mvikmpserver.model.UpdateOperacionRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class OperacionesService {

    fun list(): List<OperacionDto> = transaction {
        OperacionesTable
            .selectAll()
            .orderBy(OperacionesTable.fechaCreacion to SortOrder.DESC)
            .map(::toDto)
    }

    fun getById(id: Long): OperacionDto? = transaction {
        findById(id)
    }

    fun create(request: CreateOperacionRequest): OperacionDto = transaction {
        val id = OperacionesTable.insert {
            it[titulo] = request.titulo
            it[descripcion] = request.descripcion
            it[imagenUrl] = request.imagenUrl
            it[tipo] = request.tipo
            it[autor] = request.autor
            it[fechaCreacion] = System.currentTimeMillis()
        } get OperacionesTable.id
        findById(id) ?: error("Inserted operacion not found")
    }

    fun update(id: Long, request: UpdateOperacionRequest): OperacionDto? = transaction {
        val updated = OperacionesTable.update({ OperacionesTable.id eq id }) {
            it[titulo] = request.titulo
            it[descripcion] = request.descripcion
            it[imagenUrl] = request.imagenUrl
            it[tipo] = request.tipo
            it[autor] = request.autor
        }
        if (updated == 0) null else findById(id)
    }

    fun purchase(id: Long): OperacionDto? = transaction {
        val updated = OperacionesTable.update({ OperacionesTable.id eq id }) {
            it[guardada] = true
        }
        if (updated == 0) null else findById(id)
    }

    fun delete(id: Long): Boolean = transaction {
        OperacionesTable.deleteWhere { OperacionesTable.id eq id } > 0
    }

    private fun findById(id: Long): OperacionDto? =
        OperacionesTable
            .selectAll()
            .where { OperacionesTable.id eq id }
            .singleOrNull()
            ?.let(::toDto)

    private fun toDto(row: ResultRow): OperacionDto =
        OperacionDto(
            id = row[OperacionesTable.id],
            titulo = row[OperacionesTable.titulo],
            descripcion = row[OperacionesTable.descripcion],
            imagenUrl = row[OperacionesTable.imagenUrl],
            tipo = row[OperacionesTable.tipo],
            autor = row[OperacionesTable.autor],
            fechaCreacion = row[OperacionesTable.fechaCreacion],
            guardada = row[OperacionesTable.guardada],
        )
}
