package com.kitkat.showtime.model

class ThumbnailModel {

    private var results: List<Result>? = null

    fun getResults(): List<Result>? {
        return results
    }

    class Result {

        var backdrop_path: String? = ""

    }

    private var status_message : String? = ""

}