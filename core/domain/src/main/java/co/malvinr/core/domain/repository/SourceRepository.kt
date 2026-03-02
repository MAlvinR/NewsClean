package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Source

interface SourceRepository {
    suspend fun getSourceByCategory(category: String): Result<List<Source>>
    suspend fun getSources(category: String? = null): Result<List<Source>>
}