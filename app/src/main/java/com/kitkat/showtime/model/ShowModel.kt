package com.kitkat.showtime.model

class ShowModel {

    private var results: List<Result>? = null

    fun getResults(): List<Result>? {
        return results
    }

    class Result {

        var id: Int? = 0
        var backdrop_path: String? = ""

    }

    private var status_message : String? = ""

}