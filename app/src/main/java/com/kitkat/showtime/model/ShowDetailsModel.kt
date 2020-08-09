package com.kitkat.showtime.model


class ShowDetailsModel {

    var title: String? = ""
    var overview: String? = ""
    var poster_path: String? = ""
    var backdrop_path: String? = ""
    var name: String? = ""
    var release_date: String? = ""
    var number_of_episodes: Int? = 0
    var number_of_seasons: Int? = 0

    private var genres: List<Genres>? = null

    fun getGenres(): List<Genres>? {
        return genres
    }

    class Genres {

        var name: String? = ""
    }

    private var spoken_languages: List<SpokenLanguages>? = null

    fun getSpokenLanguages(): List<SpokenLanguages>? {
        return spoken_languages
    }

    class SpokenLanguages {

        var name: String? = ""
    }

}