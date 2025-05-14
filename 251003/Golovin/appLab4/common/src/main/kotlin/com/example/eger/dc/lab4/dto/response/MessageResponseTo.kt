package com.example.eger.dc.lab4.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseTo(
	private val id: Long?, private val country: String?, private val storyId: Long, private val content: String?
)