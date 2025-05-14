package com.example.raw.dc.lab2.dao.impl

import com.example.raw.dc.lab2.bean.Marker
import com.example.raw.dc.lab2.dao.MarkerDao
import com.example.raw.dc.lab2.database.Markers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class MarkerDaoImpl(private val connection: Connection) : MarkerDao {
	init {
		val statement = connection.createStatement()
		statement.executeUpdate(Markers.CREATE_TABLE_MARKERS)
	}

	private fun ResultSet.getString(value: Markers): String = getString("$value")
	private fun ResultSet.getLong(value: Markers): Long = getLong("$value")

	override suspend fun create(item: Marker): Long = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Markers.INSERT_MARKER, Statement.RETURN_GENERATED_KEYS)
		statement.apply {
			setString(1, item.name)
			executeUpdate()
		}

		val generatedKeys = statement.generatedKeys
		if (generatedKeys.next()) {
			return@withContext generatedKeys.getLong(1)
		} else {
			throw Exception("Unable to retrieve the id of the newly inserted item.")
		}
	}

	override suspend fun deleteById(id: Long): Int = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Markers.DELETE_MARKER)
		statement.setLong(1, id)

		return@withContext try {
			statement.executeUpdate()
		} catch (_: Exception) {
			throw Exception("Can not delete item record.")
		}
	}

	override suspend fun getAll(): List<Marker> = withContext(Dispatchers.IO) {
		val result = mutableListOf<Marker>()
		val statement = connection.prepareStatement(Markers.SELECT_MARKERS)

		val resultSet = statement.executeQuery()
		while (resultSet.next()) {
			val id = resultSet.getLong(Markers.COLUMN_ID)
			val name = resultSet.getString(Markers.COLUMN_NAME)
			result.add(Marker(id, name))
		}

		result
	}

	override suspend fun getById(id: Long): Marker = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Markers.SELECT_MARKER_BY_ID)
		statement.setLong(1, id)

		val resultSet = statement.executeQuery()
		if (resultSet.next()) {
			val name = resultSet.getString(Markers.COLUMN_NAME)
			return@withContext Marker(id, name)
		} else {
			throw Exception("Item record not found.")
		}
	}

	override suspend fun update(item: Marker): Int = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Markers.UPDATE_MARKER)
		statement.apply {
			setString(1, item.name)
			item.id?.let { setLong(2, it) }
		}

		return@withContext try {
			statement.executeUpdate()
		} catch (_: Exception) {
			throw Exception("Can not modify item record.")
		}
	}
}