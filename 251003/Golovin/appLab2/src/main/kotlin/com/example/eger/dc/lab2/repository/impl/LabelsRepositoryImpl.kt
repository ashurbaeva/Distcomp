package com.example.eger.dc.lab2.repository.impl

import com.example.eger.dc.lab2.bean.Label
import com.example.eger.dc.lab2.dao.LabelDao
import com.example.eger.dc.lab2.repository.LabelsRepository

class LabelsRepositoryImpl(
	private val dao: LabelDao
) : LabelsRepository {
	override suspend fun create(item: Label): Long? {
		return try {
			dao.create(item)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun deleteById(id: Long): Boolean = dao.deleteById(id) > 0

	override suspend fun getAll(): List<Label?> = dao.getAll()

	override suspend fun getById(id: Long): Label? {
		return try {
			dao.getById(id)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun update(item: Label): Boolean = dao.update(item) > 0
}