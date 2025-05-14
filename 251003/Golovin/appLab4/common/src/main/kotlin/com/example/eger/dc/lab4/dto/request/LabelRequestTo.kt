package com.example.eger.dc.lab4.dto.request

import com.example.eger.dc.lab4.bean.Label
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