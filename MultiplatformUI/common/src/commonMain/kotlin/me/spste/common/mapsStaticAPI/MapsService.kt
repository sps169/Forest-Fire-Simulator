package com.forestfiresimulatortfg.mapsStaticAPI

import com.forestfiresimulatortfg.mapsStaticAPI.RetrofitClient.getClient
import utils.getLocalProperty
import okhttp3.ResponseBody

object MapsService {
    val mapsApi : MapsApi = getClient().create(MapsApi::class.java)
    val mapProps = "getMap.properties"

    fun getMap(location: String): ResponseBody? {
        val response = mapsApi.getMap(
            "staticmap?" +
                    "key=${getLocalProperty("api_key")}"
                    + "&center=$location" +
                    "&zoom=${getLocalProperty("default_zoom", mapProps)}" +
                    "&format=${getLocalProperty("format", mapProps)}" +
                    "&maptype=${getLocalProperty("maptype", mapProps)}" +
                    "&size=${getLocalProperty("default_size", mapProps)}" +
                    "&style=element:labels%7Cvisibility:off" +
                    "&style=feature:administrative%7Cvisibility:off" +
                    "&style=feature:landscape%7Celement:geometry%7Ccolor:0x2fa745" +
                    "&style=feature:poi%7Cvisibility:off" +
                    "&style=feature:road%7Ccolor:0xffed4d%7Cweight:0.5" +
                    "&style=feature:transit%7Ccolor:0xffed4d" +
                    "&style=feature:transit.line%7Cvisibility:off" +
                    "&style=feature:water%7Celement:geometry%7Ccolor:0x5eb3e8"
        ).execute()
        return response.body()

    }
}