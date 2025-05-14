package com.example.eger.dc.lab1.service

import com.example.eger.dc.lab1.dto.request.MarkerRequestTo
import com.example.eger.dc.lab1.dto.request.MarkerRequestToId
import com.example.eger.dc.lab1.dto.response.MarkerResponseTo

interface MarkerService {
	suspend fun create(requestTo: MarkerRequestTo?): MarkerResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<MarkerResponseTo>

	suspend fun getById(id: Long): MarkerResponseTo?

	suspend fun update(requestTo: MarkerRequestToId?): MarkerResponseTo?
}