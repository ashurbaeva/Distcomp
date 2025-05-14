package com.example.raw.dc.lab1.repository

import com.example.raw.dc.lab1.bean.Marker

interface MarkersRepository {
	val data: MutableList<Pair<Long, Marker>>

	suspend fun create(id: Long, item: Marker): Marker?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<Marker?>

	suspend fun getById(id: Long): Marker?

	suspend fun getNextId(): Long?
}