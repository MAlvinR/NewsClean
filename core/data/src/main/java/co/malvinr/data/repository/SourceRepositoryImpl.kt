package co.malvinr.data.repository

import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.repository.SourceRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : SourceRepository {

    override suspend fun getSources(category: String?): Result<List<Source>> =
        runCatching {
            networkDataSource.getSources().articles.toDomainList()
        }

    override suspend fun getSourceByCategory(category: String): Result<List<Source>> =
        runCatching {
            networkDataSource.getSources(category).articles.toDomainList()
        }
}