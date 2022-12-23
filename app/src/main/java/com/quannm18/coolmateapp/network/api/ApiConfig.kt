package com.quannm18.coolmateapp.network.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        const val BASE_URL = "http://20.205.154.231:3000/"
        private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}