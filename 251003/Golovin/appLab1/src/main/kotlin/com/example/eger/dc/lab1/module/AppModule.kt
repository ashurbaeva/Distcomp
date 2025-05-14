package com.example.eger.dc.lab1.module

import com.example.eger.dc.lab1.repository.WritersRepository
import com.example.eger.dc.lab1.repository.StoriesRepository
import com.example.eger.dc.lab1.repository.MessagesRepository
import com.example.eger.dc.lab1.repository.MarkersRepository
import com.example.eger.dc.lab1.service.WriterService
import com.example.eger.dc.lab1.service.StoryService
import com.example.eger.dc.lab1.service.MessageService
import com.example.eger.dc.lab1.service.MarkerService
import com.example.eger.dc.lab1.service.impl.AuthorServiceImpl
import com.example.eger.dc.lab1.service.impl.IssueServiceImpl
import com.example.eger.dc.lab1.service.impl.MessageServiceImpl
import com.example.eger.dc.lab1.service.impl.StickerServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<WriterService> {
		val repository: WritersRepository = get(authorsRepositoryQualifier)

		AuthorServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(issuesRepositoryQualifier)

		IssueServiceImpl(repository)
	}
	single<MessageService> {
		val repository: MessagesRepository = get(messagesRepositoryQualifier)

		MessageServiceImpl(repository)
	}
	single<MarkerService> {
		val repository: MarkersRepository = get(stickersRepositoryQualifier)

		StickerServiceImpl(repository)
	}
}