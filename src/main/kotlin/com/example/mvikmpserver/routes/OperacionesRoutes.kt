package com.example.mvikmpserver.routes

import com.example.mvikmpserver.model.CreateOperacionRequest
import com.example.mvikmpserver.model.UpdateOperacionRequest
import com.example.mvikmpserver.service.OperacionesService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.operacionesRoutes(service: OperacionesService) {
    route("/operaciones") {
        get {
            call.respond(service.list())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid id"))
                return@get
            }
            val operacion = service.getById(id)
            if (operacion == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Operacion not found"))
            } else {
                call.respond(operacion)
            }
        }

        post {
            val request = call.receive<CreateOperacionRequest>()
            val created = service.create(request)
            call.respond(HttpStatusCode.Created, created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid id"))
                return@put
            }
            val request = call.receive<UpdateOperacionRequest>()
            val updated = service.update(id, request)
            if (updated == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Operacion not found"))
            } else {
                call.respond(updated)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid id"))
                return@delete
            }
            if (service.delete(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Operacion not found"))
            }
        }
    }
}
