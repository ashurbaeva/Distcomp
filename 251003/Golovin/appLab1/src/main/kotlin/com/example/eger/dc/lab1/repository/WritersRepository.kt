package com.example.eger.dc.lab1.repository

import com.example.eger.dc.lab1.bean.Writer

interface WritersRepository {
	val data: MutableList<Pair<Long, Writer>>

	suspend fun create(id: Long, item: Writer): Writer?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<Writer?>

	suspend fun getById(id: Long): Writer?

	suspend fun getNextId(): Long?
}