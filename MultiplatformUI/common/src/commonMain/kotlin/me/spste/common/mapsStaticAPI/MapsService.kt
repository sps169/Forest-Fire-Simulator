package me.spste.common.mapsStaticAPI

import com.forestfiresimulatortfg.mapsStaticAPI.RetrofitClient.getClient
import me.spste.common.utils.PropertiesHandler
import okhttp3.ResponseBody

const val DISPLAY_OPTION = 0
const val ANALYSIS_OPTION = 1

class MapsService(val propertyHandler: PropertiesHandler) {
    val mapsApi : MapsApi = getClient().create(MapsApi::class.java)
    val mapProps = "getMap.properties"
    fun getMap(location: String, option: Int): ResponseBody? {
        val queue = "staticmap?" +
                "key=${propertyHandler.getLocalProperty("api_key", "local.properties")}" +
                "&center=$location" +
                "&zoom=${propertyHandler.getLocalProperty("default_zoom", mapProps)} " +
                "&format=${propertyHandler.getLocalProperty("format", mapProps)}" +
                "&maptype=${propertyHandler.getLocalProperty("maptype", mapProps)}" +
                "&size=${propertyHandler.getLocalProperty("default_size", mapProps)}"
        val style =
            if (option == DISPLAY_OPTION)
                propertyHandler.getLocalProperty("style_display", mapProps)
            else
                propertyHandler.getLocalProperty("style_analysis", mapProps)

        return mapsApi.getMap(queue + style).execute().body()

    }
}