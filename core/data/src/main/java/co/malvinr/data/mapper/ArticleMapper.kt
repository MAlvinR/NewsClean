package co.malvinr.data.mapper

import co.malvinr.core.domain.Article
import co.malvinr.network.model.ArticlesListResponse

fun ArticlesListResponse.toDomain(): Article = Article(
    title = title,
    description = description,
    thumbUrl = urlToImage,
    url = url,
    publishedAt = publishedAt
)

fun List<ArticlesListResponse>.toDomainList(): List<Article> = map { it.toDomain() }