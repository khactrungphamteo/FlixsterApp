package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "DetailActivity"
private const val YOUTUBE_API_KEY = "AIzaSyA4OtD3JhIhOw5eieiqke3MTSwZtv0Mv4c"
private const val TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var vtPlayerView: YouTubePlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // set up connection between Activity and Layout
        tvTitle = findViewById(R.id.tvTitle)
        tvOverView = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAvarage)
        vtPlayerView = findViewById(R.id.player) // resolve the player view from the layout

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie

        Log.i(TAG, "Movie is $movie")
        tvTitle.text = movie.title
        tvOverView.text = movie.overview
        // TODO: get rating of the movies
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILER_URL.format(movie.movieId), object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG,"onSucess: JSON data $json")
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    if (results.length() == 0) {
                        Log.e(TAG, "No movies trailers found")
                        return
                    }
                    val movieTrailerJson = results.getJSONObject(1)
                    val youtubeKey = movieTrailerJson.getString("key")

                    // play the youtube video with this trailer
                    initializeYoutube(youtubeKey)

                } catch(e: JSONException) {
                    Log.e(TAG, "Encountered Exception $e")
                }
            }

        })
    }

    private fun initializeYoutube(youtubeKey: String) {
        // initalize with API key stored in secrets.xml
        vtPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializedListener")
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializedFailure")
            }
        })
    }
}