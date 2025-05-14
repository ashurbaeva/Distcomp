package com.example.eger.dc.lab2.service

import com.example.eger.dc.lab2.dto.request.LabelRequestTo
import com.example.eger.dc.lab2.dto.request.LabelRequestToId
import com.example.eger.dc.lab2.dto.response.LabelResponseTo

interface LabelService {
	suspend fun create(requestTo: LabelRequestTo?): LabelResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<LabelResponseTo>

	suspend fun getById(id: Long): LabelResponseTo?

	suspend fun update(requestTo: LabelRequestToId?): LabelResponseTo?
}