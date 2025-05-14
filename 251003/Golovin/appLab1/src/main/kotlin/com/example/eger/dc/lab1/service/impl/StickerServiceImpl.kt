package com.example.eger.dc.lab1.service.impl

import com.example.eger.dc.lab1.dto.request.MarkerRequestTo
import com.example.eger.dc.lab1.dto.request.MarkerRequestToId
import com.example.eger.dc.lab1.dto.response.MarkerResponseTo
import com.example.eger.dc.lab1.repository.MarkersRepository
import com.example.eger.dc.lab1.service.MarkerService

class StickerServiceImpl(
	private val repository: MarkersRepository
) : MarkerService {
	override suspend fun create(requestTo: MarkerRequestTo?): MarkerResponseTo? {
		val id = repository.getNextId() ?: return null
		val bean = requestTo?.toBean(id) ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<MarkerResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): MarkerResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: MarkerRequestToId?): MarkerResponseTo? {
		val bean = requestTo?.toBean() ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}
}