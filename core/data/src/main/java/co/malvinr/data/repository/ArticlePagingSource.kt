package co.malvinr.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.malvinr.core.domain.model.Article
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.NetworkDataSource
import java.io.IOException

class ArticlePagingSource(
    private val networkDataSource: NetworkDataSource,
    private val country: String,
    private val sources: String,
    private val query: String
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: STARTING_PAGE
        val pageSize = params.loadSize

        return try {
            val response = networkDataSource.getTopHeadlines(
                country = country,
                sources = sources,
                query = query,
                page = page,
                pageSize = params.loadSize
            )

            val articles = response.articles.toDomainList()

            val prevKey = if (page == STARTING_PAGE) null else page - 1
            val itemsLoaded = (page - STARTING_PAGE) * pageSize + response.articles.size
            val nextKey = if (itemsLoaded >= response.totalResults) null else page + 1

            LoadResult.Page(
                data = articles,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE = 1
    }
}
