package com.example.raw.dc.lab3.repository.impl

import com.example.raw.dc.lab3.bean.Writer
import com.example.raw.dc.lab3.dao.WriterDao
import com.example.raw.dc.lab3.repository.WritersRepository

class WritersRepositoryImpl(
	private val dao: WriterDao
) : WritersRepository {
	override suspend fun create(item: Writer): Long? {
		return try {
			dao.create(item)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun deleteById(id: Long): Boolean = dao.deleteById(id) > 0

	override suspend fun getAll(): List<Writer?> = dao.getAll()

	override suspend fun getById(id: Long): Writer? {
		return try {
			dao.getById(id)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun update(item: Writer): Boolean = dao.update(item) > 0
}