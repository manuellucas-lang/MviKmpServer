package com.example.mvikmpserver

import com.example.mvikmpserver.db.DatabaseFactory
import com.example.mvikmpserver.routes.operacionesRoutes
import com.example.mvikmpserver.service.OperacionesService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    DatabaseFactory.init()
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            },
        )
    }
    install(CallLogging)
    install(CORS) {
        anyHost()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled error", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Internal server error")),
            )
        }
    }

    val service = OperacionesService()

    routing {
        get("/") {
            call.respond(
                mapOf(
                    "name" to "MviKmpServerExample",
                    "status" to "running",
                    "endpoints" to "/operaciones",
                ),
            )
        }
        operacionesRoutes(service)
    }
}
