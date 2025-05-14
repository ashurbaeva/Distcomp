package com.example.eger.dc.lab1.service.impl

import com.example.eger.dc.lab1.dto.request.WriterRequestTo
import com.example.eger.dc.lab1.dto.request.WriterRequestToId
import com.example.eger.dc.lab1.dto.response.WriterResponseTo
import com.example.eger.dc.lab1.repository.WritersRepository
import com.example.eger.dc.lab1.service.WriterService

class AuthorServiceImpl(
	private val repository: WritersRepository
) : WriterService {
	override suspend fun create(requestTo: WriterRequestTo?): WriterResponseTo? {
		val id = repository.getNextId() ?: return null
		val bean = requestTo?.toBean(id) ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<WriterResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): WriterResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: WriterRequestToId?): WriterResponseTo? {
		val bean = requestTo?.toBean() ?: return null
		val result = repository.create(bean.id, bean) ?: return null

		return result.toResponse()
	}
}