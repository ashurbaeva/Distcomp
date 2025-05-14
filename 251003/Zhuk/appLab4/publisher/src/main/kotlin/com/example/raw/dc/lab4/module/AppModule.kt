package com.example.raw.dc.lab4.module

import com.example.raw.dc.lab4.repository.WritersRepository
import com.example.raw.dc.lab4.repository.StoriesRepository
import com.example.raw.dc.lab4.repository.MarkersRepository
import com.example.raw.dc.lab4.service.WriterService
import com.example.raw.dc.lab4.service.StoryService
import com.example.raw.dc.lab4.service.MarkerService
import com.example.raw.dc.lab4.service.impl.WriterServiceImpl
import com.example.raw.dc.lab4.service.impl.StoryServiceImpl
import com.example.raw.dc.lab4.service.impl.MarkerServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<WriterService> {
		val repository: WritersRepository = get(authorsRepositoryQualifier)

		WriterServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(issuesRepositoryQualifier)

		StoryServiceImpl(repository)
	}
	single<MarkerService> {
		val repository: MarkersRepository = get(stickersRepositoryQualifier)

		MarkerServiceImpl(repository)
	}
}