package com.kitkat.showtime.utilities

class Constants {

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3"

        private const val MOVIE = "/movie"
        private const val TV = "/tv"

        const val MOVIE_TOP_RATED = "$BASE_URL$MOVIE/top_rated"
        const val MOVIE_UPCOMING = "$BASE_URL$MOVIE/upcoming"
        const val MOVIE_POPULAR = "$BASE_URL$MOVIE/popular"
        const val MOVIE_TRENDING_DAILY = "$BASE_URL/trending$MOVIE/day"
        const val MOVIE_TRENDING_WEEKLY = "$BASE_URL/trending$MOVIE/week"
        const val MOVIE_DETAILS = "$BASE_URL$MOVIE/"

        const val TV_TOP_RATED = "$BASE_URL$TV/top_rated"
        const val TV_UPCOMING = "$BASE_URL$TV/airing_today"
        const val TV_POPULAR = "$BASE_URL$TV/popular"
        const val TV_TRENDING_DAILY = "$BASE_URL/trending$TV/day"
        const val TV_TRENDING_WEEKLY = "$BASE_URL/trending$TV/week"
        const val TV_DETAILS = "$BASE_URL$TV/"

        const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

        val PAGE_NUMBER: Int = 1

    }
}