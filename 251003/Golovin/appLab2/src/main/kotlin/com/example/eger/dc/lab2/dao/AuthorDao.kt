package com.example.eger.dc.lab2.dao

import com.example.eger.dc.lab2.bean.Author

interface AuthorDao {
	suspend fun create(item: Author): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Author?>

	suspend fun getById(id: Long): Author

	suspend fun update(item: Author): Int
}