package com.test.myapplication.util

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebAntApiService {
    @GET("api/photos")
    fun loadImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("new") new: Boolean,
        @Query("popular") popular: Boolean
    ): Observable<PhotoResponse>

    @GET("api/media_objects/{id}")
    fun loadMediaResource(
        @Path("id") id: Int
    ): Call<MediaResourceModel>
}