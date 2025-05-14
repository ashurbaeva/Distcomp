package com.example.raw.dc.lab1.dto.request

import com.example.raw.dc.lab1.bean.Marker
import kotlinx.serialization.Serializable

@Serializable
data class MarkerRequestTo(
	private val name: String
) {
	fun toBean(id: Long): Marker = Marker(
		id, name
	)

	init {
		require(name.length in 2..32)
	}
}