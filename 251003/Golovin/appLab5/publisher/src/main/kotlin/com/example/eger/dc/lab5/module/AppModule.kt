package com.example.eger.dc.lab5.module

import com.example.eger.dc.lab5.repository.AuthorsRepository
import com.example.eger.dc.lab5.repository.StoriesRepository
import com.example.eger.dc.lab5.repository.LabelsRepository
import com.example.eger.dc.lab5.service.AuthorService
import com.example.eger.dc.lab5.service.IssueService
import com.example.eger.dc.lab5.service.StickerService
import com.example.eger.dc.lab5.service.impl.AuthorServiceImpl
import com.example.eger.dc.lab5.service.impl.IssueServiceImpl
import com.example.eger.dc.lab5.service.impl.StickerServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<AuthorService> {
		val repository: AuthorsRepository = get(authorsRepositoryQualifier)

		AuthorServiceImpl(repository)
	}
	single<IssueService> {
		val repository: StoriesRepository = get(issuesRepositoryQualifier)

		IssueServiceImpl(repository)
	}
	single<StickerService> {
		val repository: LabelsRepository = get(stickersRepositoryQualifier)

		StickerServiceImpl(repository)
	}
}