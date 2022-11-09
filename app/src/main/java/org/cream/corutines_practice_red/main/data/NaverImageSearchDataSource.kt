package org.cream.corutines_practice_red.main.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.cream.corutines_practice_red.main.api.NaverImageSearchService
import org.cream.corutines_practice_red.main.model.Item
import java.lang.Exception

class NaverImageSearchDataSource(
    private val query: String,
    private val imageSearchService: NaverImageSearchService
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }
    }
    // 나중에 맨션앱 리스폰스 처리한거 비교해서 봐보기 22/11/09
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val start = params.key ?: defaultStart

        return try {
            // 여러개의 API를 가져 온다면 asnyc{}를 사용하여 가져오는 것
            // asnyc{val response~}.await하면 될려나?
            val response = imageSearchService.getImages(query, params.loadSize, start)

            val items = response.items
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                start + params.loadSize
            }
            val prevKey = if (start == defaultStart) {
                null
            } else {
                start - defaultDisplay
            }
            LoadResult.Page(items, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val defaultStart = 1
        const val defaultDisplay = 10
    }
}