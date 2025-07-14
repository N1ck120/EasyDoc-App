package com.n1ck120.easydoc.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    fun init(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.0.166:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}