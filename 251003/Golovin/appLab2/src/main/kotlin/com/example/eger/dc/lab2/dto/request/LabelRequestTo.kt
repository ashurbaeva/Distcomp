package com.example.eger.dc.lab2.dto.request

import com.example.eger.dc.lab2.bean.Label
import kotlinx.serialization.Serializable

@Serializable
data class LabelRequestTo(
	private val name: String
) {
	fun toBean(id: Long?): Label = Label(
		id, name
	)

	init {
		require(name.length in 2..32)
	}
}