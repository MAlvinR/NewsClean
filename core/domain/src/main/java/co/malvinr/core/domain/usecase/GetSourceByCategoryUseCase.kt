package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.repository.SourceRepository
import javax.inject.Inject

class GetSourceByCategoryUseCase @Inject constructor(
    private val repository: SourceRepository
) {
    suspend operator fun invoke(category: String): Result<List<Source>> = repository.getSourceByCategory(category)
}