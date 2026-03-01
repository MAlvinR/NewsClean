package co.malvinr.network

import co.malvinr.network.json.AssetManager
import co.malvinr.network.model.CategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class LocalAssetDataSource @Inject constructor(
    private val networkJson: Json,
    private val assets: AssetManager
) : JsonAssetDataSource {

    override suspend fun getCategories(): List<CategoryResponse> =
        getDataFromJsonFile(CATEGORIES_ASSET)

    @OptIn(ExperimentalSerializationApi::class)
    private suspend inline fun <reified T> getDataFromJsonFile(fileName: String): List<T> =
        withContext(Dispatchers.IO) {
            assets.open(fileName).use { inputStream ->
                networkJson.decodeFromStream(inputStream)
            }
        }

    companion object {
        private const val CATEGORIES_ASSET = "categories.json"
    }
}