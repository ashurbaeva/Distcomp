package com.example.raw.dc.lab4.module

import com.example.raw.dc.lab4.repository.MessagesRepository
import com.example.raw.dc.lab4.service.MessageService
import com.example.raw.dc.lab4.service.impl.MessageServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<MessageService> {
		val repository: MessagesRepository = get(messagesRepositoryQualifier)

		MessageServiceImpl(repository)
	}
}