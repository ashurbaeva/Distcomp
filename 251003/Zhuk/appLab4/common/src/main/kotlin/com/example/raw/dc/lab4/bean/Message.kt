package com.example.raw.dc.lab4.bean

import com.example.raw.dc.lab4.dto.response.MessageResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Message(
	val id: Long?, val country: String?, val storyId: Long, val content: String?
) {
	fun toResponse(): MessageResponseTo = MessageResponseTo(id, country, storyId, content)
}