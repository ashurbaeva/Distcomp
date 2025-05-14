package com.example.raw.dc.lab4.module

import com.example.raw.dc.lab4.dao.WriterDao
import com.example.raw.dc.lab4.dao.StoryDao
import com.example.raw.dc.lab4.dao.MarkerDao
import com.example.raw.dc.lab4.dao.impl.WriterDaoImpl
import com.example.raw.dc.lab4.dao.impl.StoryDaoImpl
import com.example.raw.dc.lab4.dao.impl.MarkerDaoImpl
import com.example.raw.dc.lab4.repository.WritersRepository
import com.example.raw.dc.lab4.repository.StoriesRepository
import com.example.raw.dc.lab4.repository.MarkersRepository
import com.example.raw.dc.lab4.repository.impl.WritersRepositoryImpl
import com.example.raw.dc.lab4.repository.impl.StoriesRepositoryImpl
import com.example.raw.dc.lab4.repository.impl.MarkersRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.sql.Connection

val authorsRepositoryQualifier: StringQualifier = StringQualifier("authors_repository")
val issuesRepositoryQualifier: StringQualifier = StringQualifier("issues_repository")
val stickersRepositoryQualifier: StringQualifier = StringQualifier("stickers_repository")

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

	single<WritersRepository>(authorsRepositoryQualifier) {
		val dao = get<WriterDao>()

		WritersRepositoryImpl(dao)
	}
	single<StoriesRepository>(issuesRepositoryQualifier) {
		val dao = get<StoryDao>()

		StoriesRepositoryImpl(dao)
	}
	single<MarkersRepository>(stickersRepositoryQualifier) {
		val dao = get<MarkerDao>()

		MarkersRepositoryImpl(dao)
	}
}