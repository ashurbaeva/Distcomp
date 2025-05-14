package com.example.raw.dc.lab1.bean

import com.example.raw.dc.lab1.dto.response.MarkerResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Marker(
	val id: Long, val name: String
) {
	fun toResponse(): MarkerResponseTo = MarkerResponseTo(id, name)
}