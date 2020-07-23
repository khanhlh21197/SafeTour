package com.khanhlh.basemvvmkt.api

import com.khanhlh.basemvvmkt.model.Photos
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiInterface {

    @GET("list")
    fun getPhotos(): Observable<List<Photos>>
}