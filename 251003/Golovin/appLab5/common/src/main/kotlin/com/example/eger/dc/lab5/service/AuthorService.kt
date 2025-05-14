package com.example.eger.dc.lab5.service

import com.example.eger.dc.lab5.dto.request.AuthorRequestTo
import com.example.eger.dc.lab5.dto.request.AuthorRequestToId
import com.example.eger.dc.lab5.dto.response.AuthorResponseTo

interface AuthorService {
	suspend fun create(requestTo: AuthorRequestTo?): AuthorResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<AuthorResponseTo>

	suspend fun getById(id: Long): AuthorResponseTo?

	suspend fun update(requestTo: AuthorRequestToId?): AuthorResponseTo?
}