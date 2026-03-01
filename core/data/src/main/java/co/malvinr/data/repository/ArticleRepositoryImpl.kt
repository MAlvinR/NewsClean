package co.malvinr.data.repository

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : ArticleRepository {

    override suspend fun getArticle(): Result<List<Article>> =
        runCatching {
            networkDataSource.getTopHeadlines().articles.toDomainList()
        }

    override suspend fun searchArticle(query: String): Result<List<Article>> =
        runCatching {
            networkDataSource.searchEverything(query).articles.toDomainList()
        }

}