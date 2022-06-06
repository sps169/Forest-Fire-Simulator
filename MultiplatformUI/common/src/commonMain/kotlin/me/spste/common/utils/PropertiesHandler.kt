package me.spste.common.utils

interface PropertiesHandler {
    fun getLocalProperty(key: String, filename: String) : String?
}
