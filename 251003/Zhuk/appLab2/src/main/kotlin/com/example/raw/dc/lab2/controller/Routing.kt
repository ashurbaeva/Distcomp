package com.example.raw.dc.lab2.controller

import com.example.raw.dc.lab2.controller.routing.authorsRouting
import com.example.raw.dc.lab2.controller.routing.labelsRouting
import com.example.raw.dc.lab2.controller.routing.storiesRouting
import com.example.raw.dc.lab2.controller.routing.messagesRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	routing {
		get("/") {
			call.respondText("Hello World!")
		}
		route("/api/v1.0") {
			authorsRouting()
			storiesRouting()
			messagesRouting()
			labelsRouting()
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