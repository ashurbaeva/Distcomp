package com.example.raw.dc.lab3.repository

import com.example.raw.dc.lab3.bean.Story

interface StoriesRepository {
	suspend fun create(item: Story): Long?

	suspend fun getById(id: Long): Story?

	suspend fun getAll(): List<Story?>

	suspend fun update(item: Story): Boolean

	suspend fun deleteById(id: Long): Boolean
}