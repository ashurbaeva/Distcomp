package com.example.raw.dc.lab5.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseTo(
	private val id: Long?, private val country: String?, private val storyId: Long, var content: String?
)