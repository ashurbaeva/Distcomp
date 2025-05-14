package com.example.raw.dc.lab3.repository.impl

import com.example.raw.dc.lab3.bean.Story
import com.example.raw.dc.lab3.dao.StoryDao
import com.example.raw.dc.lab3.repository.StoriesRepository

class StoriesRepositoryImpl(
	private val dao: StoryDao
) : StoriesRepository {
	override suspend fun create(item: Story): Long? {
		return try {
			dao.create(item)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun deleteById(id: Long): Boolean = dao.deleteById(id) > 0

	override suspend fun getAll(): List<Story?> = dao.getAll()

	override suspend fun getById(id: Long): Story? {
		return try {
			dao.getById(id)
		} catch (_: Exception) {
			null
		}
	}

	override suspend fun update(item: Story): Boolean = dao.update(item) > 0
}