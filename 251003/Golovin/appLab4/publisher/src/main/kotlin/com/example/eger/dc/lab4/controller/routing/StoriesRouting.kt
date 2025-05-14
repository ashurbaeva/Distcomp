package com.example.eger.dc.lab4.controller.routing

import com.example.eger.dc.lab4.controller.respond
import com.example.eger.dc.lab4.dto.request.StoryRequestTo
import com.example.eger.dc.lab4.dto.request.StoryRequestToId
import com.example.eger.dc.lab4.sendViaKafka
import com.example.eger.dc.lab4.service.StoryService
import com.example.eger.dc.lab4.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.issuesRouting() {
	val issuesService by inject<StoryService>()

	route("/stories") {
		checkIssues(issuesService)

		createIssue(issuesService)
		deleteIssue(issuesService)
		updateIssue(issuesService)
		getIssue(issuesService)
	}
}

private fun Route.checkIssues(issuesService: StoryService) {
	get {
		val issues = issuesService.getAll()

		respond(isCorrect = {
			issues.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, issues
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})

		sendViaKafka("From Publisher: Issues GET")
	}
}

private fun Route.createIssue(issuesService: StoryService) {
	post {
		val issueRequestTo = try {
			call.receive<StoryRequestTo>()
		} catch (e: Exception) {
			null
		}

		val issue = issuesService.create(issueRequestTo)

		respond(isCorrect = {
			issue != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, issue ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.Forbidden, Response(HttpStatusCode.Forbidden.value)
			)
		})

		sendViaKafka("From Publisher: Issues POST")
	}
}

private fun Route.getIssue(issuesService: StoryService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val issue = issuesService.getById(id.toLong())

		respond(isCorrect = {
			issue != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, issue ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues GET ID")
	}
}

private fun Route.deleteIssue(issuesService: StoryService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val issue = issuesService.deleteById(id.toLong())

		respond(isCorrect = {
			issue
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

private fun Route.updateIssue(issuesService: StoryService) {
	put {
		val issueRequestToId = try {
			call.receive<StoryRequestToId>()
		} catch (e: Exception) {
			null
		}

		val issue = issuesService.update(issueRequestToId)

		respond(isCorrect = {
			issue != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, issue ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})

		sendViaKafka("From Publisher: Issues PUT")
	}
}