package co.malvinr.data.mapper

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.model.Source
import co.malvinr.network.model.ArticlesListResponse
import co.malvinr.network.model.CategoryResponse
import co.malvinr.network.model.SourceListResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

fun ArticlesListResponse.toDomain(): Article = Article(
    id = generateUUID(),
    title = title,
    description = description,
    thumbUrl = urlToImage,
    url = url,
    publishedAt = publishedAt.toArticleDateFormat()
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

fun String.toArticleDateFormat(): String {
    return try {
        val cleanedDate = if (this.contains(".")) {
            this.substringBefore(".") + "Z"
        } else {
            this
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val outputFormat = SimpleDateFormat("MMMM d", Locale.ENGLISH)

        val date = inputFormat.parse(cleanedDate)
        date?.let { outputFormat.format(it) }?: this
    } catch (e: Exception) {
        this
    }
}