package com.example.raw.dc.lab3.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseTo(
	private val id: Long?, private val country: String?, private val storyId: Long, private val content: String?
)