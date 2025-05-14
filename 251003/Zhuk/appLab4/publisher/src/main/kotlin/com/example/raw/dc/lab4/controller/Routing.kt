package com.example.raw.dc.lab4.controller

import com.example.raw.dc.lab4.controller.routing.writersRouting
import com.example.raw.dc.lab4.controller.routing.storiesRouting
import com.example.raw.dc.lab4.controller.routing.messagesRouting
import com.example.raw.dc.lab4.controller.routing.markersRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	routing {
		get("/") {
			call.respondText("Hello World!")
		}
		route("/api/v1.0") {
			writersRouting()
			storiesRouting()
			messagesRouting()
			markersRouting()
		}
	}
}

suspend fun respond(
	isCorrect: () -> Boolean, onCorrect: suspend () -> Unit, onIncorrect: suspend () -> Unit
) {
	if (isCorrect()) {
		onCorrect()
	} else {
		onIncorrect()
	}
}