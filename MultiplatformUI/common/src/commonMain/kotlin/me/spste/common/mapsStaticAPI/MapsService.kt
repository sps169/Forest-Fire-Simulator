package me.spste.common.mapsStaticAPI

import com.forestfiresimulatortfg.mapsStaticAPI.RetrofitClient.getClient
import me.spste.common.utils.getLocalProperty
import okhttp3.ResponseBody

const val DISPLAY_OPTION = 0
const val ANALISIS_OPTION = 1

object MapsService {
    val mapsApi : MapsApi = getClient().create(MapsApi::class.java)
    val mapProps = "getMap.properties"
    fun getMap(location: String, option: Int): ResponseBody? {
        val queue = "staticmap?" +
                "key=${getLocalProperty("api_key")}" +
                "&center=$location" +
                "&zoom=${getLocalProperty("default_zoom", mapProps)} " +
                "&format=${getLocalProperty("format", mapProps)}" +
                "&maptype=${getLocalProperty("maptype", mapProps)}" +
                "&size=${getLocalProperty("default_size", mapProps)}"
        val style =
            if (option == DISPLAY_OPTION)
                getLocalProperty("style_display", mapProps)
            else
                getLocalProperty("style_analysis", mapProps)

        return mapsApi.getMap(queue + style).execute().body()

    }
}