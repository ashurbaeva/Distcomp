package com.example.eger.dc.lab4.controller.routing

import com.example.eger.dc.lab4.controller.respond
import com.example.eger.dc.lab4.dto.request.LabelRequestTo
import com.example.eger.dc.lab4.dto.request.LabelRequestToId
import com.example.eger.dc.lab4.sendViaKafka
import com.example.eger.dc.lab4.service.LabelService
import com.example.eger.dc.lab4.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.stickersRouting() {
	val stickersService by inject<LabelService>()

	route("/labels") {
		checkStickers(stickersService)

		createSticker(stickersService)
		deleteSticker(stickersService)
		updateSticker(stickersService)
		getSticker(stickersService)
	}
}

private fun Route.checkStickers(stickersService: LabelService) {
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

		sendViaKafka("From Publisher: Issues GET")
	}
}

private fun Route.createSticker(stickersService: LabelService) {
	post {
		val stickerRequestTo = try {
			call.receive<LabelRequestTo>()
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

		sendViaKafka("From Publisher: Issues POST")
	}
}

private fun Route.getSticker(stickersService: LabelService) {
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

		sendViaKafka("From Publisher: Issues GET ID")
	}
}

private fun Route.deleteSticker(stickersService: LabelService) {
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

		sendViaKafka("From Publisher: Issues DELETE ID")
	}
}

private fun Route.updateSticker(stickersService: LabelService) {
	put {
		val stickerRequestToId = try {
			call.receive<LabelRequestToId>()
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

		sendViaKafka("From Publisher: Issues PUT")
	}
}