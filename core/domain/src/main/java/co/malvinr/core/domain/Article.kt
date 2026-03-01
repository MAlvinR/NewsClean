package co.malvinr.core.domain

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val thumbUrl: String,
    val url: String,
    val publishedAt: String
)