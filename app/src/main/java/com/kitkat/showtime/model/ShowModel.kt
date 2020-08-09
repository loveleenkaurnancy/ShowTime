package com.kitkat.showtime.model


class ShowModel {

    private var results: List<Result>? = null

    fun getResults(): List<Result>? {
        return results
    }

    class Result {

        var id: Int? = 0
        var poster_path: String? = ""
        var backdrop_path: String? = ""

    }

    private var status_message : String? = ""

}