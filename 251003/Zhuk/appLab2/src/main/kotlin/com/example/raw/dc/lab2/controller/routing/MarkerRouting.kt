package com.example.raw.dc.lab2.controller.routing

import com.example.raw.dc.lab2.controller.respond
import com.example.raw.dc.lab2.dto.request.MarkerRequestTo
import com.example.raw.dc.lab2.dto.request.MarkerRequestToId
import com.example.raw.dc.lab2.service.MarkerService
import com.example.raw.dc.lab2.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.labelsRouting() {
	val labelsService by inject<MarkerService>()

	route("/markers") {
		checkLabels(labelsService)

		createLabel(labelsService)
		deleteLabel(labelsService)
		updateLabel(labelsService)
		getLabel(labelsService)
	}
}

private fun Route.checkLabels(stickersService: MarkerService) {
	get {
		val stickers = stickersService.getAll()

		respond(isCorrect = {
			stickers.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, stickers
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})
	}
}

private fun Route.createLabel(stickersService: MarkerService) {
	post {
		val stickerRequestTo = try {
			call.receive<MarkerRequestTo>()
		} catch (e: Exception) {
			null
		}

		val sticker = stickersService.create(stickerRequestTo)

		respond(isCorrect = {
			sticker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, sticker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.getLabel(stickersService: MarkerService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val sticker = stickersService.getById(id.toLong())

		respond(isCorrect = {
			sticker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, sticker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.deleteLabel(stickersService: MarkerService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val sticker = stickersService.deleteById(id.toLong())

		respond(isCorrect = {
			sticker
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

private fun Route.updateLabel(stickersService: MarkerService) {
	put {
		val stickerRequestToId = try {
			call.receive<MarkerRequestToId>()
		} catch (e: Exception) {
			null
		}

		val sticker = stickersService.update(stickerRequestToId)

		respond(isCorrect = {
			sticker != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, sticker ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}