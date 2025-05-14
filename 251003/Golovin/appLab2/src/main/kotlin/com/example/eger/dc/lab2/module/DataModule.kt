package com.example.eger.dc.lab2.module

import com.example.eger.dc.lab2.dao.AuthorDao
import com.example.eger.dc.lab2.dao.StoryDao
import com.example.eger.dc.lab2.dao.MessageDao
import com.example.eger.dc.lab2.dao.LabelDao
import com.example.eger.dc.lab2.dao.impl.AuthorDaoImpl
import com.example.eger.dc.lab2.dao.impl.StoryDaoImpl
import com.example.eger.dc.lab2.dao.impl.MessageDaoImpl
import com.example.eger.dc.lab2.dao.impl.LabelDaoImpl
import com.example.eger.dc.lab2.repository.AuthorsRepository
import com.example.eger.dc.lab2.repository.StoriesRepository
import com.example.eger.dc.lab2.repository.MessagesRepository
import com.example.eger.dc.lab2.repository.LabelsRepository
import com.example.eger.dc.lab2.repository.impl.AuthorsRepositoryImpl
import com.example.eger.dc.lab2.repository.impl.StoriesRepositoryImpl
import com.example.eger.dc.lab2.repository.impl.MessagesRepositoryImpl
import com.example.eger.dc.lab2.repository.impl.LabelsRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.sql.Connection

val authorsRepositoryQualifier: StringQualifier = StringQualifier("authors_repository")
val storiesRepositoryQualifier: StringQualifier = StringQualifier("stories_repository")
val messagesRepositoryQualifier: StringQualifier = StringQualifier("messages_repository")
val labelsRepositoryQualifier: StringQualifier = StringQualifier("labels_repository")

val dataModule: Module = module {
	single<AuthorDao> {
		val dbConnection = get<Connection>()

		AuthorDaoImpl(dbConnection)
	}
	single<StoryDao> {
		val dbConnection = get<Connection>()

		StoryDaoImpl(dbConnection)
	}
	single<MessageDao> {
		val dbConnection = get<Connection>()

		MessageDaoImpl(dbConnection)
	}
	single<LabelDao> {
		val dbConnection = get<Connection>()

		LabelDaoImpl(dbConnection)
	}

	single<AuthorsRepository>(authorsRepositoryQualifier) {
		val dao = get<AuthorDao>()

		AuthorsRepositoryImpl(dao)
	}
	single<StoriesRepository>(storiesRepositoryQualifier) {
		val dao = get<StoryDao>()

		StoriesRepositoryImpl(dao)
	}
	single<MessagesRepository>(messagesRepositoryQualifier) {
		val dao = get<MessageDao>()

		MessagesRepositoryImpl(dao)
	}
	single<LabelsRepository>(labelsRepositoryQualifier) {
		val dao = get<LabelDao>()

		LabelsRepositoryImpl(dao)
	}
}