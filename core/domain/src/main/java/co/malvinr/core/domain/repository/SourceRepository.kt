package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Source

interface SourceRepository {
    suspend fun getSourceByCategory(category: String): Result<List<Source>>
}