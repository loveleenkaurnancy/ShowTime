package com.kitkat.showtime.utilities

class Constants {

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"

        const val MOVIE = BASE_URL + "movie"

        const val MOVIE_UPCOMING = "$MOVIE/upcoming"
        const val MOVIE_POPULAR = "$MOVIE/popular"
        const val MOVIE_TRENDING_DAILY = "trending/$MOVIE/day"
        const val MOVIE_TRENDING_WEEKLY = "trending/$MOVIE/week"

        const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

    }
}