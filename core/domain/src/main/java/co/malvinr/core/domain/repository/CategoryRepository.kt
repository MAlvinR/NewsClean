package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): Result<List<Category>>
}