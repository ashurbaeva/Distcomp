package com.example.eger.dc.lab2.module

import com.example.eger.dc.lab2.repository.AuthorsRepository
import com.example.eger.dc.lab2.repository.StoriesRepository
import com.example.eger.dc.lab2.repository.MessagesRepository
import com.example.eger.dc.lab2.repository.LabelsRepository
import com.example.eger.dc.lab2.service.AuthorService
import com.example.eger.dc.lab2.service.StoryService
import com.example.eger.dc.lab2.service.MessageService
import com.example.eger.dc.lab2.service.LabelService
import com.example.eger.dc.lab2.service.impl.AuthorServiceImpl
import com.example.eger.dc.lab2.service.impl.StoryServiceImpl
import com.example.eger.dc.lab2.service.impl.MessageServiceImpl
import com.example.eger.dc.lab2.service.impl.LabelServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<AuthorService> {
		val repository: AuthorsRepository = get(authorsRepositoryQualifier)

		AuthorServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(storiesRepositoryQualifier)

		StoryServiceImpl(repository)
	}
	single<MessageService> {
		val repository: MessagesRepository = get(messagesRepositoryQualifier)

		MessageServiceImpl(repository)
	}
	single<LabelService> {
		val repository: LabelsRepository = get(labelsRepositoryQualifier)

		LabelServiceImpl(repository)
	}
}