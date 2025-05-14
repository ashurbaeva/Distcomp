package com.example.eger.dc.lab1.service.impl

import com.example.eger.dc.lab1.dto.request.StoryRequestTo
import com.example.eger.dc.lab1.dto.request.StoryRequestToId
import com.example.eger.dc.lab1.dto.response.StorieResponseTo
import com.example.eger.dc.lab1.repository.StoriesRepository
import com.example.eger.dc.lab1.service.StoryService
import java.sql.Timestamp

class IssueServiceImpl(
	private val repository: StoriesRepository
) : StoryService {
	override suspend fun create(requestTo: StoryRequestTo?): StorieResponseTo? {
		val created = Timestamp(System.currentTimeMillis())
		val modified = Timestamp(System.currentTimeMillis())

		val id = repository.getNextId() ?: return null
		val bean = requestTo?.toBean(id, created, modified) ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<StorieResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): StorieResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: StoryRequestToId?): StorieResponseTo? {
		val created = Timestamp(System.currentTimeMillis())
		val modified = Timestamp(System.currentTimeMillis())

		val bean = requestTo?.toBean(created, modified) ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}
}