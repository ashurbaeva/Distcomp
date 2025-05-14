package com.example.raw.dc.lab2.service

import com.example.raw.dc.lab2.dto.request.WrtierRequestTo
import com.example.raw.dc.lab2.dto.request.WriterRequestToId
import com.example.raw.dc.lab2.dto.response.WriterResponseTo

interface WriterService {
	suspend fun create(requestTo: WrtierRequestTo?): WriterResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<WriterResponseTo>

	suspend fun getById(id: Long): WriterResponseTo?

	suspend fun update(requestTo: WriterRequestToId?): WriterResponseTo?
}