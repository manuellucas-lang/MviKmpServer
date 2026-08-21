package com.example.mvikmpserver.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {

    fun init() {
        val dbFile = File("mvikmp-server.db")
        Database.connect(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            driver = "org.sqlite.JDBC",
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(OperacionesTable)
        }
        seedIfEmpty()
    }

    private fun seedIfEmpty() {
        transaction {
            if (OperacionesTable.selectAll().empty()) {
                val now = System.currentTimeMillis()
                seedData.forEachIndexed { index, (titulo, descripcion, imagenUrl, tipo, autor) ->
                    OperacionesTable.insert { row ->
                        row[this.titulo] = titulo
                        row[this.descripcion] = descripcion
                        row[this.imagenUrl] = imagenUrl
                        row[this.tipo] = tipo
                        row[this.autor] = autor
                        row[fechaCreacion] = now + index
                    }
                }
                println("Seed: inserted ${seedData.size} sample operaciones")
            }
        }
    }

    private data class Seed(
        val titulo: String,
        val descripcion: String,
        val imagenUrl: String?,
        val tipo: String?,
        val autor: String?,
    )

    private val seedData = listOf(
        Seed(
            titulo = "Transferencia de nómina",
            descripcion = "Nómina del mes en la cuenta corriente. Actualizada automáticamente por el banco.",
            imagenUrl = "https://picsum.photos/seed/operacion1/600/400",
            tipo = "Ingreso",
            autor = "Finanzas",
        ),
        Seed(
            titulo = "Pago de alquiler de oficina",
            descripcion = "Alquiler mensual del espacio de coworking. Concepto recurrente.",
            imagenUrl = "https://picsum.photos/seed/operacion2/600/400",
            tipo = "Gasto",
            autor = "Tesorería",
        ),
        Seed(
            titulo = "Reembolso de gastos de viaje",
            descripcion = "Reembolso del viaje de negocio a la feria del sector. Justificante adjunto.",
            imagenUrl = "https://picsum.photos/seed/operacion3/600/400",
            tipo = "Reembolso",
            autor = "RRHH",
        ),
        Seed(
            titulo = "Suscripción a herramientas de desarrollo",
            descripcion = "Pago anual de las licencias del equipo de ingeniería.",
            imagenUrl = "https://picsum.photos/seed/operacion4/600/400",
            tipo = "Suscripción",
            autor = "IT",
        ),
        Seed(
            titulo = "Factura de proveedor de hosting",
            descripcion = "Coste mensual de infraestructura cloud para producción y staging.",
            imagenUrl = "https://picsum.photos/seed/operacion5/600/400",
            tipo = "Gasto",
            autor = "IT",
        ),
    )
}
