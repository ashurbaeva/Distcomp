package com.example.raw.dc.lab5.controller.routing

import com.example.raw.dc.lab5.controller.respond
import com.example.raw.dc.lab5.dto.request.WriterRequestTo
import com.example.raw.dc.lab5.dto.request.WriterRequestToId
import com.example.raw.dc.lab5.sendViaKafka
import com.example.raw.dc.lab5.service.WriterService
import com.example.raw.dc.lab5.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.writersRouting() {
	val writersService by inject<WriterService>()

	route("/writers") {
		checkAuthors(writersService)

		createAuthor(writersService)
		deleteAuthor(writersService)
		updateAuthor(writersService)
		getAuthor(writersService)
	}
}

private fun Route.checkAuthors(writersService: WriterService) {
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

		sendViaKafka("From Publisher: Authors GET")
	}
}

private fun Route.createAuthor(writersService: WriterService) {
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

		sendViaKafka("From Publisher: Authors POST")
	}
}

private fun Route.getAuthor(writersService: WriterService) {
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

		sendViaKafka("From Publisher: Authors GET ID")
	}
}

private fun Route.deleteAuthor(writersService: WriterService) {
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

		sendViaKafka("From Publisher: Authors DELETE ID")
	}
}

private fun Route.updateAuthor(writersService: WriterService) {
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

		sendViaKafka("From Publisher: Authors PUT")
	}
}