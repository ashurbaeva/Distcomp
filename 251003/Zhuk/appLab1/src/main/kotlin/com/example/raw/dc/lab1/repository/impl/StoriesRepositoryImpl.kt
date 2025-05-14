package com.example.raw.dc.lab1.repository.impl

import com.example.raw.dc.lab1.bean.Story
import com.example.raw.dc.lab1.repository.StoriesRepository

class StoriesRepositoryImpl : StoriesRepository {
	override val data: MutableList<Pair<Long, Story>> = mutableListOf()

	override suspend fun create(id: Long, item: Story): Story? {
		val flag = data.add(id to item)
		return if (flag) item else null
	}

	override suspend fun deleteById(id: Long): Boolean = data.removeIf { it.first == id }

	override suspend fun getAll(): List<Story?> = data.map { it.second }

	override suspend fun getById(id: Long): Story? = data.find { it.first == id }?.second

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