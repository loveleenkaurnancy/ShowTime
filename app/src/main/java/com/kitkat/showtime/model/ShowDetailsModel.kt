package com.kitkat.showtime.model


class ShowDetailsModel {

    var title: String? = ""
    var overview: String? = ""
    var poster_path: String? = ""
    var backdrop_path: String? = ""
    var name: String? = ""

    private var genres: List<Genres>? = null

    fun getGenres(): List<Genres>? {
        return genres
    }

    class Genres {

        var name: String? = ""
    }

}