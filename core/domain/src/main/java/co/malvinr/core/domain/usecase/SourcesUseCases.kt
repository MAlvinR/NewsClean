package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.repository.SourceRepository
import javax.inject.Inject

class GetSourceByCategoryUseCase @Inject constructor(
    private val repository: SourceRepository
) {
    suspend operator fun invoke(category: String): Result<List<Source>> = repository.getSourceByCategory(category)
}

class GetSourcesUseCase @Inject constructor(
    private val repository: SourceRepository
) {
    suspend operator fun invoke(): Result<List<Source>> = repository.getSources()
}

class SearchSourcesUseCase @Inject constructor() {
    operator fun invoke(sources: List<Source>, query: String): List<Source> =
        sources.filter {
            source -> source.name.contains(query, ignoreCase = true)
        }
}