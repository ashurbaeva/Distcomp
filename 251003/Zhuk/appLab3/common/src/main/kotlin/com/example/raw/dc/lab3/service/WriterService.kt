package com.example.raw.dc.lab3.service

import com.example.raw.dc.lab3.dto.request.WriterRequestTo
import com.example.raw.dc.lab3.dto.request.WriterRequestToId
import com.example.raw.dc.lab3.dto.response.WriterResponseTo

interface WriterService {
	suspend fun create(requestTo: WriterRequestTo?): WriterResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<WriterResponseTo>

	suspend fun getById(id: Long): WriterResponseTo?

	suspend fun update(requestTo: WriterRequestToId?): WriterResponseTo?
}