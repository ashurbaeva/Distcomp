package com.example.raw.dc.lab5.controller.routing

import com.example.raw.dc.lab5.controller.respond
import com.example.raw.dc.lab5.dto.request.MarkerRequestTo
import com.example.raw.dc.lab5.dto.request.MarkerRequestToId
import com.example.raw.dc.lab5.sendViaKafka
import com.example.raw.dc.lab5.service.MarkerService
import com.example.raw.dc.lab5.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.markersRouting() {
	val markersService by inject<MarkerService>()

	route("/markers") {
		checkStickers(markersService)

		createSticker(markersService)
		deleteSticker(markersService)
		updateSticker(markersService)
		getSticker(markersService)
	}
}

private fun Route.checkStickers(markersService: MarkerService) {
	get {
		val markers = markersService.getAll()

		respond(isCorrect = {
			markers.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, markers
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})

		sendViaKafka("From Publisher: Issues GET")
	}
}

private fun Route.createSticker(markersService: MarkerService) {
	post {
		val markerRequestTo = try {
			call.receive<MarkerRequestTo>()
		} catch (e: Exception) {
			null
		}

		val marker = markersService.create(markerRequestTo)

		respond(isCorrect = {
			marker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, marker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues POST")
	}
}

private fun Route.getSticker(markersService: MarkerService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val marker = markersService.getById(id.toLong())

		respond(isCorrect = {
			marker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, marker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues GET ID")
	}
}

private fun Route.deleteSticker(markersService: MarkerService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val marker = markersService.deleteById(id.toLong())

		respond(isCorrect = {
			marker
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.NoContent, Response(HttpStatusCode.NoContent.value)
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues DELETE ID")
	}
}

private fun Route.updateSticker(markersService: MarkerService) {
	put {
		val markerRequestToId = try {
			call.receive<MarkerRequestToId>()
		} catch (e: Exception) {
			null
		}

		val marker = markersService.update(markerRequestToId)

		respond(isCorrect = {
			marker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, marker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues PUT")
	}
}