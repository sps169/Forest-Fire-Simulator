package com.forestfiresimulatortfg.mapsStaticAPI

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MapsApi {
    @Streaming
    @GET
    fun getMap(@Url url: String) : Call<ResponseBody>
//    fun getMap(@QueryMap parameters: Map<String, String>, @QueryMap styles: Map<String, String>) : Call<ResponseBody>
}