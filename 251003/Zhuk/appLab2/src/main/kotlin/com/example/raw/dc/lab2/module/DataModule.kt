package com.example.raw.dc.lab2.module

import com.example.raw.dc.lab2.dao.WriterDao
import com.example.raw.dc.lab2.dao.StoryDao
import com.example.raw.dc.lab2.dao.MessageDao
import com.example.raw.dc.lab2.dao.MarkerDao
import com.example.raw.dc.lab2.dao.impl.WriterDaoImpl
import com.example.raw.dc.lab2.dao.impl.StoryDaoImpl
import com.example.raw.dc.lab2.dao.impl.MessageDaoImpl
import com.example.raw.dc.lab2.dao.impl.MarkerDaoImpl
import com.example.raw.dc.lab2.repository.WritersRepository
import com.example.raw.dc.lab2.repository.StoriesRepository
import com.example.raw.dc.lab2.repository.MessagesRepository
import com.example.raw.dc.lab2.repository.MarkersRepository
import com.example.raw.dc.lab2.repository.impl.WritersRepositoryImpl
import com.example.raw.dc.lab2.repository.impl.StoriesRepositoryImpl
import com.example.raw.dc.lab2.repository.impl.MessagesRepositoryImpl
import com.example.raw.dc.lab2.repository.impl.MarkersRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.sql.Connection

val authorsRepositoryQualifier: StringQualifier = StringQualifier("writers_repository")
val storiesRepositoryQualifier: StringQualifier = StringQualifier("stories_repository")
val messagesRepositoryQualifier: StringQualifier = StringQualifier("messages_repository")
val labelsRepositoryQualifier: StringQualifier = StringQualifier("markers_repository")

val dataModule: Module = module {
	single<WriterDao> {
		val dbConnection = get<Connection>()

		WriterDaoImpl(dbConnection)
	}
	single<StoryDao> {
		val dbConnection = get<Connection>()

		StoryDaoImpl(dbConnection)
	}
	single<MessageDao> {
		val dbConnection = get<Connection>()

		MessageDaoImpl(dbConnection)
	}
	single<MarkerDao> {
		val dbConnection = get<Connection>()

		MarkerDaoImpl(dbConnection)
	}

	single<WritersRepository>(authorsRepositoryQualifier) {
		val dao = get<WriterDao>()

		WritersRepositoryImpl(dao)
	}
	single<StoriesRepository>(storiesRepositoryQualifier) {
		val dao = get<StoryDao>()

		StoriesRepositoryImpl(dao)
	}
	single<MessagesRepository>(messagesRepositoryQualifier) {
		val dao = get<MessageDao>()

		MessagesRepositoryImpl(dao)
	}
	single<MarkersRepository>(labelsRepositoryQualifier) {
		val dao = get<MarkerDao>()

		MarkersRepositoryImpl(dao)
	}
}