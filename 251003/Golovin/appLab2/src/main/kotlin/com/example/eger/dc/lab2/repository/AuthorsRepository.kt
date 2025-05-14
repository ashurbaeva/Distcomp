package com.example.eger.dc.lab2.repository

import com.example.eger.dc.lab2.bean.Author

interface AuthorsRepository {
	suspend fun create(item: Author): Long?

	suspend fun getById(id: Long): Author?

	suspend fun getAll(): List<Author?>

	suspend fun update(item: Author): Boolean

	suspend fun deleteById(id: Long): Boolean
}