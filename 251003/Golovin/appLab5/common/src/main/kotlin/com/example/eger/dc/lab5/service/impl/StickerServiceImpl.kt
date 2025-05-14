package com.example.eger.dc.lab5.service.impl

import com.example.eger.dc.lab5.dto.request.LabelRequestTo
import com.example.eger.dc.lab5.dto.request.LabelRequestToId
import com.example.eger.dc.lab5.dto.response.LabelResponseTo
import com.example.eger.dc.lab5.repository.LabelsRepository
import com.example.eger.dc.lab5.service.StickerService

class StickerServiceImpl(
	private val repository: LabelsRepository
) : StickerService {
	override suspend fun create(requestTo: LabelRequestTo?): LabelResponseTo? {
		val bean = requestTo?.toBean(null) ?: return null
		val id = repository.create(bean) ?: return null
		val result = bean.copy(id = id)

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<LabelResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): LabelResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: LabelRequestToId?): LabelResponseTo? {
		val bean = requestTo?.toBean() ?: return null

		if (!repository.update(bean)) {
			throw Exception("Exception during item updating!")
		}

		val result = bean.copy()

		return result.toResponse()
	}
}