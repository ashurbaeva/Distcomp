package com.example.raw.dc.lab2.bean

import com.example.raw.dc.lab2.dto.response.MarkerResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Marker(
	val id: Long?, val name: String
) {
	fun toResponse(): MarkerResponseTo = MarkerResponseTo(id, name)
}