package com.Lee_34393862.nutritrack.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceAPIService {

    @GET("api/fruit/{fruitName}")
    suspend fun getFruit(@Path("fruitName") fruitName: String): Response<FruityViceResponseModel>

    @GET("api/fruit/all")
    suspend fun getAllFruit(): Response<List<FruityViceResponseModel>>

    companion object {

        var BASE_URL = "https://www.fruityvice.com"

        fun create(): FruityViceAPIService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(FruityViceAPIService::class.java)
        }

    }

}