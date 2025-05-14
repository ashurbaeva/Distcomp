package com.example.eger.dc.lab5.dao.impl

import com.example.eger.dc.lab5.bean.Label
import com.example.eger.dc.lab5.dao.LabelDao
import com.example.eger.dc.lab5.database.Labels
import com.example.eger.dc.lab5.id
import com.example.eger.dc.lab5.testViaRedis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class LabelDaoImpl(private val connection: Connection) : LabelDao {
	init {
		val statement = connection.createStatement()
		statement.executeUpdate(Labels.CREATE_TABLE_LABELS)
	}

	private fun ResultSet.getString(value: Labels): String = getString("$value")
	private fun ResultSet.getLong(value: Labels): Long = getLong("$value")

	override suspend fun create(item: Label): Long = withContext(Dispatchers.IO) {
		testViaRedis("${id++}", item.name)

		val statement = connection.prepareStatement(Labels.INSERT_LABEL, Statement.RETURN_GENERATED_KEYS)
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
		val statement = connection.prepareStatement(Labels.DELETE_LABEL)
		statement.setLong(1, id)

		return@withContext try {
			statement.executeUpdate()
		} catch (_: Exception) {
			throw Exception("Can not delete item record.")
		}
	}

	override suspend fun getAll(): List<Label> = withContext(Dispatchers.IO) {
		val result = mutableListOf<Label>()
		val statement = connection.prepareStatement(Labels.SELECT_LABELS)

		val resultSet = statement.executeQuery()
		while (resultSet.next()) {
			val id = resultSet.getLong(Labels.COLUMN_ID)
			val name = resultSet.getString(Labels.COLUMN_NAME)
			result.add(Label(id, name))
		}

		result
	}

	override suspend fun getById(id: Long): Label = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Labels.SELECT_LABEL_BY_ID)
		statement.setLong(1, id)

		val resultSet = statement.executeQuery()
		if (resultSet.next()) {
			val name = resultSet.getString(Labels.COLUMN_NAME)
			return@withContext Label(id, name)
		} else {
			throw Exception("Item record not found.")
		}
	}

	override suspend fun update(item: Label): Int = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Labels.UPDATE_LABEL)
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