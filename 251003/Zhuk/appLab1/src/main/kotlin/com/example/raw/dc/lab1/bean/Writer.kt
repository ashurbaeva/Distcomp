package com.example.raw.dc.lab1.bean

import com.example.raw.dc.lab1.dto.response.WriterResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Writer(
	val id: Long, val login: String, val password: String, val firstname: String, val lastname: String
) {
	fun toResponse(): WriterResponseTo = WriterResponseTo(id, login, password, firstname, lastname)
}