package com.example.raw.dc.lab1.repository

import com.example.raw.dc.lab1.bean.Story

interface StoriesRepository {
	val data: MutableList<Pair<Long, Story>>

	suspend fun create(id: Long, item: Story): Story?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<Story?>

	suspend fun getById(id: Long): Story?

	suspend fun getNextId(): Long?
}