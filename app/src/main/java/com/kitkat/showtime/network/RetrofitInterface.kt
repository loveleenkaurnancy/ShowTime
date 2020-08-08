package com.kitkat.showtime.network

import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.utilities.Constants
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET(Constants.MOVIE_UPCOMING)
    abstract fun moviesArrivingToday(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.MOVIE_POPULAR)
    abstract fun moviesPopular(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.MOVIE_TRENDING_DAILY)
    abstract fun moviesTrendingDaily(@Query("api_key") api_key : String): Call<ShowModel>

    @GET(Constants.MOVIE_TRENDING_WEEKLY)
    abstract fun moviesTrendingWeekly(@Query("api_key") api_key : String): Call<ShowModel>

}