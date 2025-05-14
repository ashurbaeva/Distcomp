package com.example.eger.dc.lab4.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class LabelResponseTo(
	private val id: Long?, private val name: String
)