package org.giuliohome.weatherforecastapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


fun getUnsafeOkHttpClient(): OkHttpClient {
    // Create a trust manager that does not perform certificate validation
    val trustAllCertificates: TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf() // Return an empty array

        override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {}

        override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {}
    }

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(trustAllCertificates), java.security.SecureRandom())

    // Create an SSL Socket Factory
    val sslSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCertificates as X509TrustManager) // Set the SSL Socket Factory
        .hostnameVerifier { _, _ -> true } // Trust all hostnames
        .build()
}

object RetrofitClient {
    private const val BASE_URL = "https://myweb.giuliohome.org/"
    private val okHttpClient = getUnsafeOkHttpClient()

    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Your base URL here
            .client(okHttpClient) // Use the custom OkHttpClient that skips SSL checks
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}