package org.cream.corutines_practice_red.main.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.cream.corutines_practice_red.main.data.NaverImageSearchRepository
import org.cream.corutines_practice_red.main.model.Item

class ImageSearchViewModel : ViewModel() {
    private val repository = NaverImageSearchRepository()
    private val queryFlow = MutableSharedFlow<String>()
    private val favorites = mutableSetOf<Item>()
    private val _favoritesFlow = MutableSharedFlow<List<Item>>(replay = 1)

    val pagingDataFlow = queryFlow // BTS를 검색하다가 ATC로 검색해서 바꾸는 그런 것
        .flatMapLatest {
            searchImages(it)
        }
        .cachedIn(viewModelScope)
    // 안드로이드가 제공하는 코루틴 스코프는 2개 viewModelScope 연수 앱에 쓴거
    // lifecycleScope

    val favoritesFlow = _favoritesFlow.asSharedFlow()

    private fun searchImages(query: String): Flow<PagingData<Item>> =
        repository.getImageSearch(query)

    fun handleQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    fun toggle(item: Item) {
        if (favorites.contains(item)) {
            favorites.remove(item)
        } else {
            favorites.add(item)
        }
        viewModelScope.launch {
            _favoritesFlow.emit(favorites.toList())
        }
    }
}