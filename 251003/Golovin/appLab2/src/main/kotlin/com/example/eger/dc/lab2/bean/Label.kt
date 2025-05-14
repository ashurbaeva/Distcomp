package com.example.eger.dc.lab2.bean

import com.example.eger.dc.lab2.dto.response.LabelResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Label(
	val id: Long?, val name: String
) {
	fun toResponse(): LabelResponseTo = LabelResponseTo(id, name)
}