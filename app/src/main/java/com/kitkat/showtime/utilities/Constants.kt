package com.kitkat.showtime.utilities

class Constants {

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3"

        private const val MOVIE = "/movie"

        const val MOVIE_UPCOMING = "$BASE_URL$MOVIE/upcoming"
        const val MOVIE_POPULAR = "$BASE_URL$MOVIE/popular"
        const val MOVIE_TRENDING_DAILY = "$BASE_URL/trending$MOVIE/day"
        const val MOVIE_TRENDING_WEEKLY = "$BASE_URL/trending$MOVIE/week"

        const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

        val PAGE_NUMBER: Int = 1

    }
}