package com.example.raw.dc.lab1.controller.routing

import com.example.raw.dc.lab1.controller.respond
import com.example.raw.dc.lab1.dto.request.WriterRequestTo
import com.example.raw.dc.lab1.dto.request.WriterRequestToId
import com.example.raw.dc.lab1.service.WriterService
import com.example.raw.dc.lab1.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authorsRouting() {
	val authorsService by inject<WriterService>()

	route("/writers") {
		checkAuthors(authorsService)

		createAuthor(authorsService)
		deleteAuthor(authorsService)
		updateAuthor(authorsService)
		getAuthor(authorsService)
	}
}

private fun Route.checkAuthors(authorsService: WriterService) {
	get {
		val authors = authorsService.getAll()

		respond(isCorrect = {
			authors.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, authors
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})
	}
}

private fun Route.createAuthor(authorsService: WriterService) {
	post {
		val authorRequestTo = try {
			call.receive<WriterRequestTo>()
		} catch (e: Exception) {
			null
		}

		val author = authorsService.create(authorRequestTo)

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, author ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.getAuthor(authorsService: WriterService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val author = authorsService.getById(id.toLong())

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, author ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.deleteAuthor(authorsService: WriterService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val author = authorsService.deleteById(id.toLong())

		respond(isCorrect = {
			author
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

private fun Route.updateAuthor(authorsService: WriterService) {
	put {
		val authorRequestToId = try {
			call.receive<WriterRequestToId>()
		} catch (e: Exception) {
			null
		}

		val author = authorsService.update(authorRequestToId)

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, author ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}