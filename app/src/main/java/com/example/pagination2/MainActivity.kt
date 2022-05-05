package com.example.pagination2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination2.adapter.MyAdapter
import com.example.pagination2.api.ApiInterface
import com.example.pagination2.databinding.ActivityMainBinding
import com.example.pagination2.modelClass.Data
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "https://api.github.com/"
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MyAdapter
    lateinit var userList: ArrayList<Data.DataItem>
    var since=0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userList = arrayListOf<Data.DataItem>()
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        myAdapter = MyAdapter(baseContext, userList)
        binding.recyclerView.adapter = myAdapter

        getData(since)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == userList.size - 1) {
                        getData(since)
                    }
                }

            }
        })

    }

    private fun getData(since:Int) {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(logger)
        isLoading = true
        binding.loading.visibility = View.VISIBLE
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(since,5)

        retrofitData.enqueue(object : Callback<ArrayList<Data.DataItem>?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Data.DataItem>?>,
                response: Response<ArrayList<Data.DataItem>?>
            ) {
                val responseBody = response.body()!!
                userList.addAll(responseBody)
                this@MainActivity.since = userList[userList.size-1].id
                binding.loading.postDelayed(Runnable {
                    myAdapter.notifyDataSetChanged()
                    isLoading = false
                    binding.loading.visibility = View.GONE
                }, 3000)
            }

            override fun onFailure(call: Call<ArrayList<Data.DataItem>?>, t: Throwable) {
                Log.d("MainActivity", "on failure" + t.message)
            }
        })
    }

}