package co.malvinr.data.mapper

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.model.Source
import co.malvinr.network.model.ArticlesListResponse
import co.malvinr.network.model.CategoryResponse
import co.malvinr.network.model.SourceListResponse
import java.util.UUID

fun ArticlesListResponse.toDomain(): Article = Article(
    id = generateUUID(),
    title = title,
    description = description,
    thumbUrl = urlToImage,
    url = url,
    publishedAt = publishedAt
)

@JvmName("toArticleDomainList")
fun List<ArticlesListResponse>.toDomainList(): List<Article> = map { it.toDomain() }

fun generateUUID(): String = UUID.randomUUID().toString()

fun CategoryResponse.toDomain(): Category = Category(
    id = id,
    name = name,
    slug = slug
)

@JvmName("toCategoryDomainList")
fun List<CategoryResponse>.toDomainList(): List<Category> = map { it.toDomain() }

fun SourceListResponse.toDomain(): Source = Source(
    id = id,
    name = name,
    url = url
)

@JvmName("toSourceDomainList")
fun List<SourceListResponse>.toDomainList(): List<Source> = map { it.toDomain() }