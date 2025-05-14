package com.example.eger.dc.lab2.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseTo(
	private val id: Long?, private val storyId: Long, private val content: String
)