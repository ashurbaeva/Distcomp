package com.example.raw.dc.lab5.controller.routing

import com.example.raw.dc.lab5.controller.respond
import com.example.raw.dc.lab5.dto.request.StoryRequestTo
import com.example.raw.dc.lab5.dto.request.StoryRequestToId
import com.example.raw.dc.lab5.sendViaKafka
import com.example.raw.dc.lab5.service.StoryService
import com.example.raw.dc.lab5.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.storiesRouting() {
	val storiesService by inject<StoryService>()

	route("/stories") {
		checkIssues(storiesService)

		createIssue(storiesService)
		deleteIssue(storiesService)
		updateIssue(storiesService)
		getIssue(storiesService)
	}
}

private fun Route.checkIssues(storiesService: StoryService) {
	get {
		val stories = storiesService.getAll()

		respond(isCorrect = {
			stories.isNotEmpty()
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, stories
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value)
			)
		})

		sendViaKafka("From Publisher: Issues GET")
	}
}

private fun Route.createIssue(storiesService: StoryService) {
	post {
		val storyRequestTo = try {
			call.receive<StoryRequestTo>()
		} catch (e: Exception) {
			null
		}

		val issue = storiesService.create(storyRequestTo)

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

private fun Route.getIssue(storiesService: StoryService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val issue = storiesService.getById(id.toLong())

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

private fun Route.deleteIssue(storiesService: StoryService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val issue = storiesService.deleteById(id.toLong())

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

private fun Route.updateIssue(storiesService: StoryService) {
	put {
		val storyRequestToId = try {
			call.receive<StoryRequestToId>()
		} catch (e: Exception) {
			null
		}

		val issue = storiesService.update(storyRequestToId)

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