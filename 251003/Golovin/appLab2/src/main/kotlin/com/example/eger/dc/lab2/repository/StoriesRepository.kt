package com.example.eger.dc.lab2.repository

import com.example.eger.dc.lab2.bean.Story

interface StoriesRepository {
	suspend fun create(item: Story): Long?

	suspend fun getById(id: Long): Story?

	suspend fun getAll(): List<Story?>

	suspend fun update(item: Story): Boolean

	suspend fun deleteById(id: Long): Boolean
}