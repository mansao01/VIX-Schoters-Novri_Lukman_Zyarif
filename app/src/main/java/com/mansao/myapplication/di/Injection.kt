package com.mansao.myapplication.di

import android.content.Context
import com.mansao.myapplication.data.NewsRepository
import com.mansao.myapplication.data.local.room.NewsDatabase
import com.mansao.myapplication.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}