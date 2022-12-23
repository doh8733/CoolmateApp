package com.quannm18.coolmateapp.network.api

import com.quannm18.coolmateapp.network.auth.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfigWithAuth {
    companion object {
        val builder = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient())
        val retrofit = builder.build()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
        private fun okhttpClient(): OkHttpClient {

            val loggingInterceptor : HttpLoggingInterceptor =  HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        }
    }
}