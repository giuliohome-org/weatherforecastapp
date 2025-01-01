package org.giuliohome.weatherforecastapp

import retrofit2.http.GET

interface WeatherApi {
    @GET("forecast/rome,italy")
    suspend fun getForecast(): List<String>

}
