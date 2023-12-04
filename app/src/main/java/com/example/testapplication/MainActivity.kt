package com.example.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = findViewById<TextView>(R.id.status)
        val pb = findViewById<ProgressBar>(R.id.pb)

        findViewById<AppCompatButton>(R.id.success).setOnClickListener {
            pb.visibility = VISIBLE
            apiService.postRequest(ExampleSkmp("description", "title"))
                .enqueue(object : Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        pb.visibility = GONE
                        status.text = response.body().toString()
                        status.setTextColor(resources.getColor(R.color.green))
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        pb.visibility = GONE
                    }
                })
        }

        findViewById<AppCompatButton>(R.id.not_found).setOnClickListener {
            pb.visibility = VISIBLE
            apiService.postRequestNotFound(body = boody).enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    status.text = "NOT FOUND. Code: 404"
                    status.setTextColor(resources.getColor(R.color.red))
                    pb.visibility = GONE
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    status.text = "NOT FOUND. Code: 404"
                    status.setTextColor(resources.getColor(R.color.red))
                    pb.visibility = GONE
                }
            })
        }

        findViewById<AppCompatButton>(R.id.error).setOnClickListener {
            pb.visibility = VISIBLE
            apiService.postRequestNotFound(boody).enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    status.setTextColor(resources.getColor(R.color.red))
                    status.text = "ERROR. Code: 500"
                    pb.visibility = GONE
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    status.setTextColor(resources.getColor(R.color.red))
                    status.text = "ERROR. Code: 500"
                    pb.visibility = GONE
                }
            })
        }
    }

    interface ApiService {
        @POST("tutorials")
        @JvmSuppressWildcards
        fun postRequest(@Body body: ExampleSkmp): Call<Response>

        @POST("tutorials")
        @JvmSuppressWildcards
        fun postRequestNotFound(@Body body: Map<String, Any>): Call<Response>

    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://194.152.37.7:4446/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService = retrofit.create(ApiService::class.java)


    val boody = mapOf(
        "title" to "description"
    )

    data class ExampleSkmp(
        val title: String,
        val description: String
    )

    data class Response(
        val id: Int,
        val title: String,
        val description: String,
        val published: Boolean
    )
}