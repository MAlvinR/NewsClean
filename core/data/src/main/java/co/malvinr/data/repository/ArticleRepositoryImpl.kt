package co.malvinr.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : ArticleRepository {

    override fun fetchTopHeadlines(
        country: String,
        sources: String
    ): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ArticlePagingSource(networkDataSource, country, sources)
            }
        ).flow

    override suspend fun searchArticle(query: String): Result<List<Article>> =
        runCatching {
            networkDataSource.searchEverything(query).articles.toDomainList()
        }
}