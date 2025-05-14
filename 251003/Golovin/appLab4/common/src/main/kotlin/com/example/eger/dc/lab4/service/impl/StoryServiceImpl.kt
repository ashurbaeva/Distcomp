package com.example.eger.dc.lab4.service.impl

import com.example.eger.dc.lab4.dto.request.StoryRequestTo
import com.example.eger.dc.lab4.dto.request.StoryRequestToId
import com.example.eger.dc.lab4.dto.response.StoryResponseTo
import com.example.eger.dc.lab4.repository.StoriesRepository
import com.example.eger.dc.lab4.service.StoryService
import java.sql.Timestamp

class StoryServiceImpl(
	private val repository: StoriesRepository
) : StoryService {
	override suspend fun create(requestTo: StoryRequestTo?): StoryResponseTo? {
		val created = Timestamp(System.currentTimeMillis())
		val modified = Timestamp(System.currentTimeMillis())

		val bean = requestTo?.toBean(null, created, modified) ?: return null
		val id = repository.create(bean) ?: return null
		val result = bean.copy(id = id)

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<StoryResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): StoryResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: StoryRequestToId?): StoryResponseTo? {
		val created = Timestamp(System.currentTimeMillis())
		val modified = Timestamp(System.currentTimeMillis())

		val bean = requestTo?.toBean(created, modified) ?: return null

		if (!repository.update(bean)) {
			throw Exception("Exception during item updating!")
		}

		val result = bean.copy()

		return result.toResponse()
	}
}