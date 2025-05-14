package com.example.raw.dc.lab3.bean

import com.example.raw.dc.lab3.dto.response.MarkerResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Marker(
	val id: Long?, val name: String
) {
	fun toResponse(): MarkerResponseTo = MarkerResponseTo(id, name)
}