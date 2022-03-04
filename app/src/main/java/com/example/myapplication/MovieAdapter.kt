package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val TAG = "MovieAdapter"
const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

        // everytime the ViewHolder is created, itemView.setOnClickLlistener() is executed
        init {
            // add this as item onClickListener
            itemView.setOnClickListener(this)
            // this refers to the class, and the class is implementing OnClickListener Interface
        }

        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            Glide.with(context).load(movie.posterImageUrl).placeholder(R.drawable.ic_launcher_foreground).into(ivPoster)
        }

        override fun onClick(p0: View?) {
            // 1. get notified of the particular movie which was clicked
            val position = adapterPosition;
            val movie: Movie = movies[position]
//            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()

            // 2. use the Intent system to navigate through the new screen
            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("movie_title", movie.title)
            // serialize the movie using Parcel
            intent.putExtra("MOVIE_EXTRA", movie)
            context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateView")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    // cheap operation
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position: $position")
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
       return movies.size
    }


}
