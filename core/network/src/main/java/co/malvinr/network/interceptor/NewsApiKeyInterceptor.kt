package co.malvinr.network.interceptor

import co.malvinr.newsclean.core.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NewsApiKeyInterceptor : Interceptor {

    private val API_KEY = BuildConfig.NEWS_API_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", API_KEY)
            .build()
        return chain.proceed(request)
    }
}