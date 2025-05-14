package com.example.raw.dc.lab3.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MarkerResponseTo(
	private val id: Long?, private val name: String
)