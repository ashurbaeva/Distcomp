package com.example.eger.dc.lab5.module

import com.example.eger.dc.lab5.dao.AuthorDao
import com.example.eger.dc.lab5.dao.StoryDao
import com.example.eger.dc.lab5.dao.LabelDao
import com.example.eger.dc.lab5.dao.impl.AuthorDaoImpl
import com.example.eger.dc.lab5.dao.impl.StoryDaoImpl
import com.example.eger.dc.lab5.dao.impl.LabelDaoImpl
import com.example.eger.dc.lab5.repository.AuthorsRepository
import com.example.eger.dc.lab5.repository.StoriesRepository
import com.example.eger.dc.lab5.repository.LabelsRepository
import com.example.eger.dc.lab5.repository.impl.AuthorsRepositoryImpl
import com.example.eger.dc.lab5.repository.impl.StoriesRepositoryImpl
import com.example.eger.dc.lab5.repository.impl.LabelsRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.sql.Connection

val authorsRepositoryQualifier: StringQualifier = StringQualifier("authors_repository")
val issuesRepositoryQualifier: StringQualifier = StringQualifier("issues_repository")
val stickersRepositoryQualifier: StringQualifier = StringQualifier("stickers_repository")

val dataModule: Module = module {
	single<AuthorDao> {
		val dbConnection = get<Connection>()

		AuthorDaoImpl(dbConnection)
	}
	single<StoryDao> {
		val dbConnection = get<Connection>()

		StoryDaoImpl(dbConnection)
	}
	single<LabelDao> {
		val dbConnection = get<Connection>()

		LabelDaoImpl(dbConnection)
	}

	single<AuthorsRepository>(authorsRepositoryQualifier) {
		val dao = get<AuthorDao>()

		AuthorsRepositoryImpl(dao)
	}
	single<StoriesRepository>(issuesRepositoryQualifier) {
		val dao = get<StoryDao>()

		StoriesRepositoryImpl(dao)
	}
	single<LabelsRepository>(stickersRepositoryQualifier) {
		val dao = get<LabelDao>()

		LabelsRepositoryImpl(dao)
	}
}