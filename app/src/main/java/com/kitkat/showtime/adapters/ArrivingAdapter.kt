package com.kitkat.showtime.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.kitkat.showtime.R
import com.kitkat.showtime.activities.NoInternetActivity
import com.kitkat.showtime.activities.ShowDetailsActivity
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.utilities.CheckNetworkStatus
import kotlinx.android.synthetic.main.item_arriving.view.*
import kotlinx.android.synthetic.main.loader.view.*
import java.util.ArrayList

class ArrivingAdapter(
    private val mValues: ArrayList<ShowModel.Result>,
    private val mContext : Context,
    private val source : String
) : RecyclerView.Adapter<ArrivingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_arriving, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        val requestOptions = RequestOptions()
        requestOptions.error(R.drawable.error)

        Glide.with(mContext)
            .setDefaultRequestOptions(requestOptions)
            .load(item.backdrop_path)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>,
                                          isFirstResource: Boolean): Boolean {
                    if (holder.progressBar.isShown()) {
                        holder.progressBar.setVisibility(View.GONE)
                    }
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>,
                                             dataSource: com.bumptech.glide.load.DataSource,
                                             isFirstResource: Boolean
                ): Boolean {
                    if (holder.progressBar.isShown()) {
                        holder.progressBar.setVisibility(View.GONE)
                    }
                    return false
                }
            })
            .into(holder.iv_backdrop_path)

        holder.iv_backdrop_path.setOnClickListener {

            if(CheckNetworkStatus.isOnline(mContext)) {
                val intent = Intent(mContext, ShowDetailsActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("source", source)
                (mContext as Activity).startActivity(intent)
            }
            else {
                val intent = Intent(mContext, NoInternetActivity::class.java)
                (mContext as Activity).startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val iv_backdrop_path: ImageView = mView.iv_backdrop_path
        val progressBar: ProgressBar = mView.progressBar

        override fun toString(): String {
            return super.toString() + " show "
        }
    }

}