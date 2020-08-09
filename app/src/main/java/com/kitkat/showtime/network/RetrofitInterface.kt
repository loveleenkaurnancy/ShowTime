package com.kitkat.showtime.network

import com.kitkat.showtime.model.ShowDetailsModel
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.utilities.Constants
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET(Constants.MOVIE_TOP_RATED)
    abstract fun moviesTopRated(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.MOVIE_UPCOMING)
    abstract fun moviesArrivingToday(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.MOVIE_POPULAR)
    abstract fun moviesPopular(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.MOVIE_TRENDING_DAILY)
    abstract fun moviesTrendingDaily(@Query("api_key") api_key : String): Call<ShowModel>

    @GET(Constants.MOVIE_TRENDING_WEEKLY)
    abstract fun moviesTrendingWeekly(@Query("api_key") api_key : String): Call<ShowModel>

    @GET(Constants.MOVIE_DETAILS + "{movie_id}")
    abstract fun moviesDetails(@Path("movie_id") movie_id : Int, @Query("api_key") api_key : String): Call<ShowDetailsModel>



    @GET(Constants.TV_TOP_RATED)
    abstract fun tvTopRated(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.TV_UPCOMING)
    abstract fun tvArrivingToday(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.TV_POPULAR)
    abstract fun tvPopular(@Query("api_key") api_key : String, @Query("page") page : Int): Call<ShowModel>

    @GET(Constants.TV_TRENDING_DAILY)
    abstract fun tvTrendingDaily(@Query("api_key") api_key : String): Call<ShowModel>

    @GET(Constants.TV_TRENDING_WEEKLY)
    abstract fun tvTrendingWeekly(@Query("api_key") api_key : String): Call<ShowModel>

    @GET(Constants.TV_DETAILS + "{tv_id}")
    abstract fun tvDetails(@Path("tv_id") movie_id : Int, @Query("api_key") api_key : String): Call<ShowDetailsModel>

}