package com.example.raw.dc.lab1.service

import com.example.raw.dc.lab1.dto.request.StoryRequestTo
import com.example.raw.dc.lab1.dto.request.StoryRequestToId
import com.example.raw.dc.lab1.dto.response.StorieResponseTo

interface StoryService {
	suspend fun create(requestTo: StoryRequestTo?): StorieResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<StorieResponseTo>

	suspend fun getById(id: Long): StorieResponseTo?

	suspend fun update(requestTo: StoryRequestToId?): StorieResponseTo?
}