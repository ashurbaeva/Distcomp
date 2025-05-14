package com.example.eger.dc.lab4.module

import com.example.eger.dc.lab4.repository.AuthorsRepository
import com.example.eger.dc.lab4.repository.StoriesRepository
import com.example.eger.dc.lab4.repository.LabelsRepository
import com.example.eger.dc.lab4.service.AuthorService
import com.example.eger.dc.lab4.service.StoryService
import com.example.eger.dc.lab4.service.LabelService
import com.example.eger.dc.lab4.service.impl.AuthorServiceImpl
import com.example.eger.dc.lab4.service.impl.StoryServiceImpl
import com.example.eger.dc.lab4.service.impl.LabelServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<AuthorService> {
		val repository: AuthorsRepository = get(authorsRepositoryQualifier)

		AuthorServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(issuesRepositoryQualifier)

		StoryServiceImpl(repository)
	}
	single<LabelService> {
		val repository: LabelsRepository = get(stickersRepositoryQualifier)

		LabelServiceImpl(repository)
	}
}