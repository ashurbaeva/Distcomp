package com.example.raw.dc.lab4.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class WriterResponseTo(
	private val id: Long?,
	private val login: String,
	private val password: String,
	private val firstname: String,
	private val lastname: String
)