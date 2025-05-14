package com.example.eger.dc.lab2.dto.request

import com.example.eger.dc.lab2.bean.Label
import kotlinx.serialization.Serializable

@Serializable
data class LabelRequestToId(
	private val id: Long, private val name: String
) {
	fun toBean(): Label = Label(
		id, name
	)

	init {
		require(name.length in 2..32)
	}
}