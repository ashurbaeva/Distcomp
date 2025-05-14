package com.example.raw.dc.lab4.dao

import com.example.raw.dc.lab4.bean.Story

interface StoryDao {
	suspend fun create(item: Story): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Story?>

	suspend fun getById(id: Long): Story

	suspend fun update(item: Story): Int
}