package co.malvinr.data.mapper

import co.malvinr.core.domain.Article
import co.malvinr.network.model.ArticlesListResponse
import java.util.UUID

fun ArticlesListResponse.toDomain(): Article = Article(
    id = generateUUID("$title|$publishedAt"),
    title = title,
    description = description,
    thumbUrl = urlToImage,
    url = url,
    publishedAt = publishedAt
)

fun List<ArticlesListResponse>.toDomainList(): List<Article> = map { it.toDomain() }

fun generateUUID(value: String): String = UUID.nameUUIDFromBytes(value.toByteArray()).toString()