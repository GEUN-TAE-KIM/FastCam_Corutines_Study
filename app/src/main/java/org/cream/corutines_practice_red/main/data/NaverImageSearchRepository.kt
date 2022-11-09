package org.cream.corutines_practice_red.main.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.cream.corutines_practice_red.main.api.NaverImageSearchService
import org.cream.corutines_practice_red.main.model.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NaverImageSearchRepository {
    private val service: NaverImageSearchService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                        // addHeader로 내 api 아이디와 시크릿을 설정
                    .addHeader("X-Naver-Client-Id", "6TbuwtiCng3kgKidaI_9")
                    .addHeader("X-Naver-Client-Secret","9nHU87Ry__")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logger)
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverImageSearchService::class.java)
    }

    fun getImageSearch(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = NaverImageSearchDataSource.defaultDisplay,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverImageSearchDataSource(query, service)
            }
        ).flow
    }
}