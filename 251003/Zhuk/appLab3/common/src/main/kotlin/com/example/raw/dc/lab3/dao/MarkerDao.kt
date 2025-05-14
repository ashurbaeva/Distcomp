package com.example.raw.dc.lab3.dao

import com.example.raw.dc.lab3.bean.Marker

interface MarkerDao {
	suspend fun create(item: Marker): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Marker?>

	suspend fun getById(id: Long): Marker

	suspend fun update(item: Marker): Int
}