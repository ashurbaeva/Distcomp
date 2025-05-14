package com.example.eger.dc.lab5.bean

import com.example.eger.dc.lab5.dto.response.LabelResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Label(
	val id: Long?, val name: String
) {
	fun toResponse(): LabelResponseTo = LabelResponseTo(id, name)
}