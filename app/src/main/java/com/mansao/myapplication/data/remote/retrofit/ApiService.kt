package com.mansao.myapplication.data.remote.retrofit

import com.mansao.myapplication.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?country=us")
    suspend fun getNews(
        @Query("apiKey")
        apiKey: String
    ): NewsResponse

    @GET("everything?sortBy=popularity")
    suspend fun searchNews(
        @Query("q")
        q: String,
        @Query("apiKey")
        apiKey: String
    ): NewsResponse
}