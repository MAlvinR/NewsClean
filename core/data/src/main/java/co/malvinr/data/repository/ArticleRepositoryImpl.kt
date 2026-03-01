package co.malvinr.data.repository

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.api.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : ArticleRepository {

    override suspend fun getArticle(): Result<List<Article>> =
        runCatching {
            apiService.getTopHeadlines("us").articles.toDomainList()
        }

    override fun searchArticle(query: String): Flow<Result<List<Article>>> = flow {
        emit(
            runCatching {
                apiService.searchEverything(query).articles.toDomainList()
            }
        )
    }.flowOn(Dispatchers.IO)
}