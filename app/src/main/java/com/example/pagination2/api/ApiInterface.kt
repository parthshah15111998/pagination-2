package com.example.pagination2.api

import com.example.pagination2.modelClass.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    //    @GET("users?page=1&per_page=5")
    @GET("users")
    fun getData(
        @Query("since")since:Int,
        @Query("per_page")per_page:Int): Call<ArrayList<Data.DataItem>>

}