package co.malvinr.data.mapper

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.model.Category
import co.malvinr.network.model.ArticlesListResponse
import co.malvinr.network.model.CategoryResponse
import java.util.UUID

fun ArticlesListResponse.toDomain(): Article = Article(
    id = generateUUID("$title|$publishedAt"),
    title = title,
    description = description,
    thumbUrl = urlToImage,
    url = url,
    publishedAt = publishedAt
)

@JvmName("toArticleDomainList")
fun List<ArticlesListResponse>.toDomainList(): List<Article> = map { it.toDomain() }

fun generateUUID(value: String): String = UUID.nameUUIDFromBytes(value.toByteArray()).toString()

fun CategoryResponse.toDomain(): Category = Category(
    id = id,
    name = name,
    slug = slug
)

@JvmName("toCategoryDomainList")
fun List<CategoryResponse>.toDomainList(): List<Category> = map { it.toDomain() }