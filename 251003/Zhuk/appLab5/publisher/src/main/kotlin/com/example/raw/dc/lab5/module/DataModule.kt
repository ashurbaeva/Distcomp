package com.example.raw.dc.lab5.module

import com.example.raw.dc.lab5.dao.WriterDao
import com.example.raw.dc.lab5.dao.StoryDao
import com.example.raw.dc.lab5.dao.MarkerDao
import com.example.raw.dc.lab5.dao.impl.WriterDaoImpl
import com.example.raw.dc.lab5.dao.impl.StoryDaoImpl
import com.example.raw.dc.lab5.dao.impl.MarkerDaoImpl
import com.example.raw.dc.lab5.repository.WritersRepository
import com.example.raw.dc.lab5.repository.StoriesRepository
import com.example.raw.dc.lab5.repository.MarkersRepository
import com.example.raw.dc.lab5.repository.impl.WritersRepositoryImpl
import com.example.raw.dc.lab5.repository.impl.StoriesRepositoryImpl
import com.example.raw.dc.lab5.repository.impl.MarkersRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.sql.Connection

val writersRepositoryQualifier: StringQualifier = StringQualifier("writers_repository")
val storiesRepositoryQualifier: StringQualifier = StringQualifier("stories_repository")
val markersRepositoryQualifier: StringQualifier = StringQualifier("markers_repository")

val dataModule: Module = module {
	single<WriterDao> {
		val dbConnection = get<Connection>()

		WriterDaoImpl(dbConnection)
	}
	single<StoryDao> {
		val dbConnection = get<Connection>()

		StoryDaoImpl(dbConnection)
	}
	single<MarkerDao> {
		val dbConnection = get<Connection>()

		MarkerDaoImpl(dbConnection)
	}

	single<WritersRepository>(writersRepositoryQualifier) {
		val dao = get<WriterDao>()

		WritersRepositoryImpl(dao)
	}
	single<StoriesRepository>(storiesRepositoryQualifier) {
		val dao = get<StoryDao>()

		StoriesRepositoryImpl(dao)
	}
	single<MarkersRepository>(markersRepositoryQualifier) {
		val dao = get<MarkerDao>()

        com.example.raw.dc.lab5.repository.impl.MarkersRepositoryImpl(dao)
	}
}