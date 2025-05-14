package com.example.eger.dc.lab5.dao

import com.example.eger.dc.lab5.bean.Label

interface LabelDao {
	suspend fun create(item: Label): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Label?>

	suspend fun getById(id: Long): Label

	suspend fun update(item: Label): Int
}