package com.example.raw.dc.lab3.module

import com.example.raw.dc.lab3.repository.WritersRepository
import com.example.raw.dc.lab3.repository.StoriesRepository
import com.example.raw.dc.lab3.repository.MarkersRepository
import com.example.raw.dc.lab3.service.WriterService
import com.example.raw.dc.lab3.service.StoryService
import com.example.raw.dc.lab3.service.MarkerService
import com.example.raw.dc.lab3.service.impl.WriterServiceImpl
import com.example.raw.dc.lab3.service.impl.StoryServiceImpl
import com.example.raw.dc.lab3.service.impl.MarkerServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
	single<WriterService> {
		val repository: WritersRepository = get(writersRepositoryQualifier)

		WriterServiceImpl(repository)
	}
	single<StoryService> {
		val repository: StoriesRepository = get(storiesRepositoryQualifier)

		StoryServiceImpl(repository)
	}
	single<MarkerService> {
		val repository: MarkersRepository = get(markersRepositoryQualifier)

		MarkerServiceImpl(repository)
	}
}