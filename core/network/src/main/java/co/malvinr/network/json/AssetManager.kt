package co.malvinr.network.json

import java.io.InputStream

fun interface AssetManager {
    fun open(fileName: String): InputStream
}