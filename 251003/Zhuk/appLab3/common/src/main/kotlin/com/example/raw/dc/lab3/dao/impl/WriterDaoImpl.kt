package com.example.raw.dc.lab3.dao.impl

import com.example.raw.dc.lab3.bean.Writer
import com.example.raw.dc.lab3.dao.WriterDao
import com.example.raw.dc.lab3.database.Writers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class WriterDaoImpl(private val connection: Connection) : WriterDao {
	init {
		val statement = connection.createStatement()
		statement.executeUpdate(Writers.CREATE_TABLE_WRITERS)
	}

	private fun ResultSet.getString(value: Writers): String = getString("$value")
	private fun ResultSet.getLong(value: Writers): Long = getLong("$value")

	override suspend fun create(item: Writer): Long = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Writers.INSERT_WRITER, Statement.RETURN_GENERATED_KEYS)
		statement.apply {
			setString(1, item.login)
			setString(2, item.password)
			setString(3, item.firstname)
			setString(4, item.lastname)
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
		val statement = connection.prepareStatement(Writers.DELETE_WRITER)
		statement.setLong(1, id)

		return@withContext try {
			statement.executeUpdate()
		} catch (_: Exception) {
			throw Exception("Can not delete item record.")
		}
	}

	override suspend fun getAll(): List<Writer?> = withContext(Dispatchers.IO) {
		val result = mutableListOf<Writer>()
		val statement = connection.prepareStatement(Writers.SELECT_WRITERS)

		val resultSet = statement.executeQuery()
		while (resultSet.next()) {
			val id = resultSet.getLong(Writers.COLUMN_ID)
			val login = resultSet.getString(Writers.COLUMN_LOGIN)
			val password = resultSet.getString(Writers.COLUMN_PASSWORD)
			val firstname = resultSet.getString(Writers.COLUMN_FIRSTNAME)
			val lastname = resultSet.getString(Writers.COLUMN_LASTNAME)
			result.add(
				Writer(
					id = id, login = login, password = password, firstname = firstname, lastname = lastname
				)
			)
		}

		result
	}

	override suspend fun getById(id: Long): Writer = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Writers.SELECT_WRITER_BY_ID)
		statement.setLong(1, id)

		val resultSet = statement.executeQuery()
		if (resultSet.next()) {
			val login = resultSet.getString(Writers.COLUMN_LOGIN)
			val password = resultSet.getString(Writers.COLUMN_PASSWORD)
			val firstname = resultSet.getString(Writers.COLUMN_FIRSTNAME)
			val lastname = resultSet.getString(Writers.COLUMN_LASTNAME)
			return@withContext Writer(
				id = id, login = login, password = password, firstname = firstname, lastname = lastname
			)
		} else {
			throw Exception("Item record not found.")
		}
	}

	override suspend fun update(item: Writer): Int = withContext(Dispatchers.IO) {
		val statement = connection.prepareStatement(Writers.UPDATE_WRITER)
		statement.apply {
			setString(1, item.login)
			setString(2, item.password)
			setString(3, item.firstname)
			setString(4, item.lastname)
			item.id?.let { setLong(5, it) }
		}

		return@withContext try {
			statement.executeUpdate()
		} catch (_: Exception) {
			throw Exception("Can not modify item record.")
		}
	}
}