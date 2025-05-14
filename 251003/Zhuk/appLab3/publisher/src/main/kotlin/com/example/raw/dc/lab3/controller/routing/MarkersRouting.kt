package com.example.raw.dc.lab3.controller.routing

import com.example.raw.dc.lab3.controller.respond
import com.example.raw.dc.lab3.dto.request.MarkerRequestTo
import com.example.raw.dc.lab3.dto.request.MarkerRequestToId
import com.example.raw.dc.lab3.service.MarkerService
import com.example.raw.dc.lab3.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.markersRouting() {
	val markersService by inject<MarkerService>()

	route("/markers") {
		checkMarkers(markersService)

		createMarker(markersService)
		deleteMarker(markersService)
		updateMarker(markersService)
		getMarker(markersService)
	}
}

private fun Route.checkMarkers(markersService: MarkerService) {
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
	}
}

private fun Route.createMarker(markersService: MarkerService) {
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
	}
}

private fun Route.getMarker(markersService: MarkerService) {
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
	}
}

private fun Route.deleteMarker(markersService: MarkerService) {
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
	}
}

private fun Route.updateMarker(markersService: MarkerService) {
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
	}
}