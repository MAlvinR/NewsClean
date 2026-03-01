package co.malvinr.data.repository

import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.repository.CategoryRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.JsonAssetDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val jsonAssetDataSource: JsonAssetDataSource
) : CategoryRepository {

    override suspend fun fetchCategories(): Result<List<Category>> =
        runCatching {
            jsonAssetDataSource.getCategories().toDomainList()
        }
}