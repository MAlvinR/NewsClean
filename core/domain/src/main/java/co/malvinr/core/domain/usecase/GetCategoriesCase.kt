package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> = repository.fetchCategories()
}