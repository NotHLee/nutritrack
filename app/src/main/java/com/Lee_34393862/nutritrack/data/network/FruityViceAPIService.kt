package com.Lee_34393862.nutritrack.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceAPIService {

    // id will be used because we already cached the name and ids initially
    // and there is bug where some fruits cannot be found using its name directly
    // for example: Ceylon Gooseberry
    @GET("api/fruit/{fruitId}")
    suspend fun getFruit(@Path("fruitId") fruitId: Int): Response<FruityViceResponseModel>

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