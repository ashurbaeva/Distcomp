package com.example.eger.dc.lab1.module

import com.example.eger.dc.lab1.repository.WritersRepository
import com.example.eger.dc.lab1.repository.StoriesRepository
import com.example.eger.dc.lab1.repository.MessagesRepository
import com.example.eger.dc.lab1.repository.MarkersRepository
import com.example.eger.dc.lab1.repository.impl.WritersRepositoryImpl
import com.example.eger.dc.lab1.repository.impl.StoriesRepositoryImpl
import com.example.eger.dc.lab1.repository.impl.MessagesRepositoryImpl
import com.example.eger.dc.lab1.repository.impl.MarkersRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val authorsRepositoryQualifier: StringQualifier = StringQualifier("authors_repository")
val issuesRepositoryQualifier: StringQualifier = StringQualifier("issues_repository")
val messagesRepositoryQualifier: StringQualifier = StringQualifier("messages_repository")
val stickersRepositoryQualifier: StringQualifier = StringQualifier("stickers_repository")

val dataModule: Module = module {
	single<WritersRepository>(authorsRepositoryQualifier) {
		WritersRepositoryImpl()
	}
	single<StoriesRepository>(issuesRepositoryQualifier) {
		StoriesRepositoryImpl()
	}
	single<MessagesRepository>(messagesRepositoryQualifier) {
		MessagesRepositoryImpl()
	}
	single<MarkersRepository>(stickersRepositoryQualifier) {
		MarkersRepositoryImpl()
	}
}