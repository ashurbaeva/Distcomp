package com.example.raw.dc.lab5.service

import com.example.raw.dc.lab5.dto.request.StoryRequestTo
import com.example.raw.dc.lab5.dto.request.StoryRequestToId
import com.example.raw.dc.lab5.dto.response.StoryResponseTo

interface StoryService {
	suspend fun create(requestTo: StoryRequestTo?): StoryResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<StoryResponseTo>

	suspend fun getById(id: Long): StoryResponseTo?

	suspend fun update(requestTo: StoryRequestToId?): StoryResponseTo?
}