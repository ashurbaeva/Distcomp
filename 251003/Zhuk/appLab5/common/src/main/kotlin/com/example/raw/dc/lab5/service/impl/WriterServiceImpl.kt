package com.example.raw.dc.lab5.service.impl

import com.example.raw.dc.lab5.dto.request.WriterRequestTo
import com.example.raw.dc.lab5.dto.request.WriterRequestToId
import com.example.raw.dc.lab5.dto.response.WriterResponseTo
import com.example.raw.dc.lab5.repository.WritersRepository
import com.example.raw.dc.lab5.service.WriterService

class WriterServiceImpl(
	private val repository: WritersRepository
) : WriterService {
	override suspend fun create(requestTo: WriterRequestTo?): WriterResponseTo? {
		val bean = requestTo?.toBean(null) ?: return null
		val id = repository.create(bean) ?: return null
		val result = bean.copy(id = id)

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

		if (!repository.update(bean)) {
			throw Exception("Exception during item updating!")
		}

		val result = bean.copy()

		return result.toResponse()
	}
}