package com.example.eger.dc.lab4.repository

import com.example.eger.dc.lab4.bean.Label

interface LabelsRepository {
	suspend fun create(item: Label): Long?

	suspend fun getById(id: Long): Label?

	suspend fun getAll(): List<Label?>

	suspend fun update(item: Label): Boolean

	suspend fun deleteById(id: Long): Boolean
}