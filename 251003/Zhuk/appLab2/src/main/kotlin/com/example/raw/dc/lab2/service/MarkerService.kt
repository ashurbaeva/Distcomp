package com.example.raw.dc.lab2.service

import com.example.raw.dc.lab2.dto.request.MarkerRequestTo
import com.example.raw.dc.lab2.dto.request.MarkerRequestToId
import com.example.raw.dc.lab2.dto.response.MarkerResponseTo

interface MarkerService {
	suspend fun create(requestTo: MarkerRequestTo?): MarkerResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<MarkerResponseTo>

	suspend fun getById(id: Long): MarkerResponseTo?

	suspend fun update(requestTo: MarkerRequestToId?): MarkerResponseTo?
}