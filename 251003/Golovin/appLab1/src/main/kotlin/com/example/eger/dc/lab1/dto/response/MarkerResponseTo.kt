package com.example.eger.dc.lab1.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MarkerResponseTo(
	private val id: Long, private val name: String
)