package com.example.raw.dc.lab5.repository.impl

import com.example.raw.dc.lab5.bean.Marker
import com.example.raw.dc.lab5.dao.MarkerDao
import com.example.raw.dc.lab5.repository.MarkersRepository

class MarkersRepositoryImpl(
	private val dao: MarkerDao
) : MarkersRepository {
	override suspend fun create(item: Marker): Long? {
		return try {
			dao.create(item)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun deleteById(id: Long): Boolean = dao.deleteById(id) > 0

	override suspend fun getAll(): List<Marker?> = dao.getAll()

	override suspend fun getById(id: Long): Marker? {
		return try {
			dao.getById(id)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun update(item: Marker): Boolean = dao.update(item) > 0
}