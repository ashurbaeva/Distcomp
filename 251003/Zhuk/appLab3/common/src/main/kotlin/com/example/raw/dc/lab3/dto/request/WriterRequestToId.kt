package com.example.raw.dc.lab3.dto.request

import com.example.raw.dc.lab3.bean.Writer
import kotlinx.serialization.Serializable

@Serializable
data class WriterRequestToId(
	private val id: Long,
	private val login: String,
	private val password: String,
	private val firstname: String,
	private val lastname: String
) {
	fun toBean(): Writer = Writer(id, login, password, firstname, lastname)

	init {
		require(login.length in 2..64)
		require(password.length in 8..128)
		require(firstname.length in 2..64)
		require(lastname.length in 2..64)
	}
}