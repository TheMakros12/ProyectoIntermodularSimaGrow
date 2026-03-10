package com.example.proyectosimagrow.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance  {

    private const val BASE_URL = "http://68.221.171.14/simagrow/"

    val instance: SimagrowApiServices by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SimagrowApiServices::class.java)
    }

}