package org.giuliohome.weatherforecastapp

import retrofit2.http.Path
import retrofit2.http.GET


interface WeatherApi {
    @GET("forecast/{city},{country}")
    suspend fun getForecast(
        @Path("city") city: String,
        @Path("country") country: String
    ) : List<String>

}

