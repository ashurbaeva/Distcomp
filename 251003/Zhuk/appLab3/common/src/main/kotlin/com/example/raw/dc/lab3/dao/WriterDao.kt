package com.example.raw.dc.lab3.dao

import com.example.raw.dc.lab3.bean.Writer

interface WriterDao {
	suspend fun create(item: Writer): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Writer?>

	suspend fun getById(id: Long): Writer

	suspend fun update(item: Writer): Int
}