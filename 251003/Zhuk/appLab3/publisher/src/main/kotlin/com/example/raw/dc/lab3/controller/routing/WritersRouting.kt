package com.example.raw.dc.lab3.controller.routing

import com.example.raw.dc.lab3.controller.respond
import com.example.raw.dc.lab3.dto.request.WriterRequestTo
import com.example.raw.dc.lab3.dto.request.WriterRequestToId
import com.example.raw.dc.lab3.service.WriterService
import com.example.raw.dc.lab3.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.writersRouting() {
	val writersService by inject<WriterService>()

	route("/writers") {
		checkWriters(writersService)

		createWriter(writersService)
		deleteWriter(writersService)
		updateWriter(writersService)
		getWriter(writersService)
	}
}

private fun Route.checkWriters(writersService: WriterService) {
	get {
		val writers = writersService.getAll()

		respond(isCorrect = {
			writers.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, writers
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})
	}
}

private fun Route.createWriter(writersService: WriterService) {
	post {
		val writerRequestTo = try {
			call.receive<WriterRequestTo>()
		} catch (e: Exception) {
			null
		}

		val writer = writersService.create(writerRequestTo)

		respond(isCorrect = {
			writer != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, writer ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.Forbidden, Response(HttpStatusCode.Forbidden.value)
			)
		})
	}
}

private fun Route.getWriter(writersService: WriterService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val writer = writersService.getById(id.toLong())

		respond(isCorrect = {
			writer != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, writer ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.deleteWriter(writersService: WriterService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val writer = writersService.deleteById(id.toLong())

		respond(isCorrect = {
			writer
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

private fun Route.updateWriter(writersService: WriterService) {
	put {
		val writerRequestToId = try {
			call.receive<WriterRequestToId>()
		} catch (e: Exception) {
			null
		}

		val writer = writersService.update(writerRequestToId)

		respond(isCorrect = {
			writer != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, writer ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}