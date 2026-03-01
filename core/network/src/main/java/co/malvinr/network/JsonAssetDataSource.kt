package co.malvinr.network

import co.malvinr.network.model.CategoryResponse

interface JsonAssetDataSource {
    suspend fun getCategories(): List<CategoryResponse>
}