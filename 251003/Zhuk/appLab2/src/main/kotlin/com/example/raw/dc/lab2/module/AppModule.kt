package com.example.raw.dc.lab2.module

import com.example.raw.dc.lab2.repository.WritersRepository
import com.example.raw.dc.lab2.repository.StoriesRepository
import com.example.raw.dc.lab2.repository.MessagesRepository
import com.example.raw.dc.lab2.repository.MarkersRepository
import com.example.raw.dc.lab2.service.WriterService
import com.example.raw.dc.lab2.service.StoryService
import com.example.raw.dc.lab2.service.MessageService
import com.example.raw.dc.lab2.service.MarkerService
import com.example.raw.dc.lab2.service.impl.WriterServiceImpl
import com.example.raw.dc.lab2.service.impl.StoryServiceImpl
import com.example.raw.dc.lab2.service.impl.MessageServiceImpl
import com.example.raw.dc.lab2.service.impl.MarkerServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<WriterService> {
		val repository: WritersRepository = get(authorsRepositoryQualifier)

		WriterServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(storiesRepositoryQualifier)

		StoryServiceImpl(repository)
	}
	single<MessageService> {
		val repository: MessagesRepository = get(messagesRepositoryQualifier)

		MessageServiceImpl(repository)
	}
	single<MarkerService> {
		val repository: MarkersRepository = get(labelsRepositoryQualifier)

		MarkerServiceImpl(repository)
	}
}