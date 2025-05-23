package com.example.raw.dc.lab1.repository.impl

import com.example.raw.dc.lab1.bean.Writer
import com.example.raw.dc.lab1.repository.WritersRepository

class WritersRepositoryImpl : com.example.raw.dc.lab1.repository.WritersRepository {
	override val data: MutableList<Pair<Long, Writer>> = mutableListOf()

	override suspend fun create(id: Long, item: Writer): Writer? {
		val flag = data.add(id to item)
		return if (flag) item else null
	}

	override suspend fun deleteById(id: Long): Boolean = data.removeIf { it.first == id }

	override suspend fun getAll(): List<Writer?> = data.map { it.second }

	override suspend fun getById(id: Long): Writer? = data.find { it.first == id }?.second

	override suspend fun getNextId(): Long? {
		return if (data.isEmpty()) {
			-1
		} else {
			var maxKey = 0L

			data.forEach { maxKey = maxOf(it.first, maxKey) }

			data.find { it.first == maxKey }?.second?.id ?: return null
		} + 1
	}
}