package com.example.eger.dc.lab5.service

import com.example.eger.dc.lab5.dto.request.MessageRequestTo
import com.example.eger.dc.lab5.dto.request.MessageRequestToId
import com.example.eger.dc.lab5.dto.response.MessageResponseTo

interface MessageService {
	suspend fun create(requestTo: MessageRequestTo?): MessageResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<MessageResponseTo>

	suspend fun getById(id: Long): MessageResponseTo?

	suspend fun update(requestTo: MessageRequestToId?): MessageResponseTo?
}