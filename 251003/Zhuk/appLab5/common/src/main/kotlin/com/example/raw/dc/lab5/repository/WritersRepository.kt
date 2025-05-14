package com.example.raw.dc.lab5.repository

import com.example.raw.dc.lab5.bean.Writer

interface WritersRepository {
	suspend fun create(item: Writer): Long?

	suspend fun getById(id: Long): Writer?

	suspend fun getAll(): List<Writer?>

	suspend fun update(item: Writer): Boolean

	suspend fun deleteById(id: Long): Boolean
}