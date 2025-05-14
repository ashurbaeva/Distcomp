package com.example.raw.dc.lab5.module

import com.example.raw.dc.lab5.repository.MessagesRepository
import com.example.raw.dc.lab5.service.MessageService
import com.example.raw.dc.lab5.service.impl.MessageServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<MessageService> {
		val repository: MessagesRepository = get(messagesRepositoryQualifier)

		MessageServiceImpl(repository)
	}
}