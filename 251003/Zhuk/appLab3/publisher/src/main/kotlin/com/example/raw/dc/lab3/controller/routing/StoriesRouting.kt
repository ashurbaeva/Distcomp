package com.example.raw.dc.lab3.controller.routing

import com.example.raw.dc.lab3.controller.respond
import com.example.raw.dc.lab3.dto.request.StoryRequestTo
import com.example.raw.dc.lab3.dto.request.StoryRequestToId
import com.example.raw.dc.lab3.service.StoryService
import com.example.raw.dc.lab3.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.storiesRouting() {
	val storiesService by inject<StoryService>()

	route("/stories") {
		checkStorys(storiesService)

		createStory(storiesService)
		deleteStory(storiesService)
		updateStory(storiesService)
		getStory(storiesService)
	}
}

private fun Route.checkStorys(storiesService: StoryService) {
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
	}
}

private fun Route.createStory(storiesService: StoryService) {
	post {
		val storyRequestTo = try {
			call.receive<StoryRequestTo>()
		} catch (e: Exception) {
			null
		}

		val story = storiesService.create(storyRequestTo)

		respond(isCorrect = {
			story != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.Created, story ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.Forbidden, Response(HttpStatusCode.Forbidden.value)
			)
		})
	}
}

private fun Route.getStory(storiesService: StoryService) {
	get("/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val story = storiesService.getById(id.toLong())

		respond(isCorrect = {
			story != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, story ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.deleteStory(storiesService: StoryService) {
	delete("/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest, message = Response(HttpStatusCode.BadRequest.value)
		)

		val story = storiesService.deleteById(id.toLong())

		respond(isCorrect = {
			story
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

private fun Route.updateStory(storiesService: StoryService) {
	put {
		val storyRequestToId = try {
			call.receive<StoryRequestToId>()
		} catch (e: Exception) {
			null
		}

		val story = storiesService.update(storyRequestToId)

		respond(isCorrect = {
			story != null
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.OK, story ?: return@respond
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}