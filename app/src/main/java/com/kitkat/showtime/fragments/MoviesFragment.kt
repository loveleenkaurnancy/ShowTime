package com.kitkat.showtime.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitkat.showtime.R
import com.kitkat.showtime.adapters.ThumbnailAdapter
import com.kitkat.showtime.model.ThumbnailModel
import com.kitkat.showtime.network.RetrofitClient
import com.kitkat.showtime.utilities.CheckNetworkStatus
import com.kitkat.showtime.utilities.Constants
import kotlinx.android.synthetic.main.fragment_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MoviesFragment : Fragment() {

    lateinit var rv_arriving_today : RecyclerView
    private val arrayList = ArrayList<ThumbnailModel.Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_movies, container, false)

        rv_arriving_today = view.findViewById(R.id.rv_arriving_today)
        rv_arriving_today.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        rv_arriving_today.setNestedScrollingEnabled(false)

        moviesArrivingToday()

        return view
    }

    private fun moviesArrivingToday() {

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.moviesArrivingToday(resources.getString(R.string.api_key), "1").enqueue(object :
                Callback<ThumbnailModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ThumbnailModel>, response: Response<ThumbnailModel>) {

                    Log.e("Response", response.body().toString())

                    when {
                        response.code() == 401 -> {
                            rl_arriving_today.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            rl_arriving_today.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rl_arriving_today.visibility = View.GONE
                            }
                            else {
                                for (i in 0 until size) {

                                    val thumbnailModel = ThumbnailModel.Result()

                                    thumbnailModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList.add(thumbnailModel)

                                }

                                rv_arriving_today.adapter = ThumbnailAdapter(arrayList, requireContext())
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ThumbnailModel>, th: Throwable) {

                    Log.e("Response", th.message)

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            

        }

    }

}